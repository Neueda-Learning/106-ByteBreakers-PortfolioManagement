package com.neueda.portfolio.diversify.service;

import com.neueda.portfolio.diversify.dto.SuggestionDTO;
import com.neueda.portfolio.diversify.exception.DiversifyBadRequestException;
import com.neueda.portfolio.diversify.exception.DiversifyServiceException;
import com.neueda.portfolio.diversify.model.Holdin;
import com.neueda.portfolio.diversify.model.Option;
import com.neueda.portfolio.diversify.repository.HoldingsRepository;
import com.neueda.portfolio.diversify.repository.OptionRepository;
import org.springframework.dao.DataAccessException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Reads from current_holdings now instead of raw transaction rows --
 * that table is already one row per stock with totals, so no per-lot
 * summing happens here anymore (it used to, before current_holdings
 * existed).
 */
@Service
public class DiversificationService {

    private final HoldingsRepository currentHoldingsRepo;
    private final OptionRepository optionRepo;

    @Value("${diversify.weight.concentration:0.40}")
    private double weightConcentration;

    @Value("${diversify.weight.market:0.45}")
    private double weightMarket;

    @Value("${diversify.weight.personal:0.15}")
    private double weightPersonal;

    @Value("${diversify.volatility-penalty-strength:0.30}")
    private double volatilityPenaltyStrength;

    @Value("${diversify.overweight-multiplier:1.5}")
    private double overweightMultiplier;

    public DiversificationService(HoldingsRepository currentHoldingsRepo,
                                  OptionRepository optionRepo) {
        this.currentHoldingsRepo = currentHoldingsRepo;
        this.optionRepo = optionRepo;
    }

    public List<SuggestionDTO> getSuggestions(int topN) {
        if (topN <= 0) {
            throw new DiversifyBadRequestException("topN must be greater than 0.");
        }
        if (topN > 100) {
            throw new DiversifyBadRequestException("topN must be less than or equal to 100.");
        }

        try {
            List<Holdin> holdings = currentHoldingsRepo.findAllWithOption();
            List<Option> allOptions = optionRepo.findAll();
            if (allOptions.isEmpty()) {
                throw new DiversifyServiceException("No investment options available to generate suggestions.");
            }

            Map<String, Double> currentAllocationPct = computeCurrentAllocation(holdings);
            Map<String, Double> avgReturnByCategory = computePersonalReturns(holdings);
            Map<Long, Double> trendScoreByOption = computeTrendScores(allOptions);
            Map<Long, Double> rawMarketSignal = computeRawMarketSignal(allOptions, trendScoreByOption);
            Map<Long, Double> marketNorm = normalize(rawMarketSignal);
            Map<Long, Double> volatilityByOption = allOptions.stream()
                    .collect(Collectors.toMap(Option::getId, this::volatilityOrDefault));
            Map<Long, Double> volatilityNorm = normalize(volatilityByOption);

            Set<String> allCategories = new HashSet<>(currentAllocationPct.keySet());
            allOptions.forEach(o -> allCategories.add(normalizeCategory(o.getCategory())));
            double targetAllocation = allCategories.isEmpty() ? 0 : 1.0 / allCategories.size();

            List<SuggestionDTO> suggestions = new ArrayList<>();

            for (Option option : allOptions) {
                String category = normalizeCategory(option.getCategory());
                double currentPct = currentAllocationPct.getOrDefault(category, 0.0);
                double concentrationGap = targetAllocation - currentPct;
                boolean overweight = currentPct > targetAllocation * overweightMultiplier;

                double marketScore = marketNorm.getOrDefault(option.getId(), 0.5);

                double personalRaw = avgReturnByCategory.getOrDefault(category, 0.0);
                double personalClipped = Math.max(-0.5, Math.min(0.5, personalRaw));
                double personalScoreNorm = personalClipped + 0.5;

                double rawScore =
                        weightConcentration * Math.max(concentrationGap, 0)
                        + weightMarket * marketScore
                        + weightPersonal * personalScoreNorm;

                double riskPenalty = volatilityPenaltyStrength * volatilityNorm.getOrDefault(option.getId(), 0.5);
                double finalScore = rawScore * (1 - riskPenalty);

                if (overweight) {
                    finalScore *= 0.2;
                }

                String reason = buildReason(category, currentPct, targetAllocation,
                        trendScoreByOption.getOrDefault(option.getId(), 0.0),
                        avgReturnByCategory.get(category),
                        volatilityOrDefault(option));

                suggestions.add(new SuggestionDTO(
                        option.getId(), option.getName(), category,
                        Math.round(finalScore * 10000.0) / 10000.0,
                        reason
                ));
            }

            suggestions.sort((a, b) -> Double.compare(b.getScore(), a.getScore()));
            return suggestions.stream().limit(topN).collect(Collectors.toList());
        } catch (DataAccessException ex) {
            throw ex;
        } catch (DiversifyBadRequestException | DiversifyServiceException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new DiversifyServiceException("Failed to generate diversification suggestions.", ex);
        }
    }

    // -----------------------------------------------------------------
    // Signal computation -- now over CurrentHolding (one row per stock)
    // instead of individual lots, since current_holdings is pre-aggregated.
    // -----------------------------------------------------------------

    private Map<String, Double> computeCurrentAllocation(List<Holdin> holdings) {
        Map<String, Double> valueByCategory = new HashMap<>();
        double total = 0.0;
        for (Holdin h : holdings) {
            double qty = h.getTotalQuantityOwned().doubleValue();
            double currentPrice = h.getInvestmentOption().getCurrentPrice().doubleValue();
            double value = qty * currentPrice;
            String category = normalizeCategory(h.getInvestmentOption().getCategory());
            valueByCategory.merge(category, value, Double::sum);
            total += value;
        }
        double finalTotal = total == 0 ? 1.0 : total;
        Map<String, Double> pctByCategory = new HashMap<>();
        valueByCategory.forEach((cat, val) -> pctByCategory.put(cat, val / finalTotal));
        return pctByCategory;
    }

    /**
     * Personal return per category, now computed straight from the
     * aggregate: (current_value - total_invested) / total_invested per
     * stock, averaged per category -- no more per-lot loop needed.
     */
    private Map<String, Double> computePersonalReturns(List<Holdin> holdings) {
        Map<String, List<Double>> returnsByCategory = new HashMap<>();
        for (Holdin h : holdings) {
            double invested = h.getTotalInvested().doubleValue();
            if (invested == 0) continue;
            double qty = h.getTotalQuantityOwned().doubleValue();
            double currentValue = qty * h.getInvestmentOption().getCurrentPrice().doubleValue();
            double returnPct = (currentValue - invested) / invested;
            returnsByCategory.computeIfAbsent(normalizeCategory(h.getInvestmentOption().getCategory()), k -> new ArrayList<>())
                    .add(returnPct);
        }
        Map<String, Double> avgByCategory = new HashMap<>();
        returnsByCategory.forEach((cat, list) ->
                avgByCategory.put(cat, list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0)));
        return avgByCategory;
    }

    private Map<Long, Double> computeTrendScores(List<Option> options) {
        Map<Long, Double> scores = new HashMap<>();
        for (Option o : options) {
            scores.put(o.getId(), trendToScore(o.getTrend()));
        }
        return scores;
    }

    private double trendToScore(String trend) {
        if (trend == null) return 0.0;
        switch (trend) {
            case "strong_up": return 1.0;
            case "up": return 0.5;
            case "flat": return 0.0;
            case "down": return -0.5;
            case "strong_down": return -1.0;
            default: return 0.0;
        }
    }

    private Map<Long, Double> computeRawMarketSignal(List<Option> options, Map<Long, Double> trendScores) {
        Map<Long, Double> raw = new HashMap<>();
        for (Option o : options) {
            double estReturn = o.getEstimatedReturn() != null ? o.getEstimatedReturn().doubleValue() : 0.0;
            double trendScore = trendScores.getOrDefault(o.getId(), 0.0);
            raw.put(o.getId(), 0.6 * trendScore + 0.4 * estReturn);
        }
        return raw;
    }

    private double volatilityOrDefault(Option option) {
        return option.getVolatility() != null ? option.getVolatility().doubleValue() : 0.2;
    }

    private Map<Long, Double> normalize(Map<Long, Double> values) {
        if (values.isEmpty()) return values;
        double min = Collections.min(values.values());
        double max = Collections.max(values.values());
        Map<Long, Double> result = new HashMap<>();
        if (max == min) {
            values.keySet().forEach(k -> result.put(k, 0.5));
            return result;
        }
        for (Map.Entry<Long, Double> e : values.entrySet()) {
            result.put(e.getKey(), (e.getValue() - min) / (max - min));
        }
        return result;
    }

    private String buildReason(String category, double currentPct, double targetAllocation,
                                double trendScore, Double avgPersonalReturn, double volatility) {
        List<String> parts = new ArrayList<>();
        if (targetAllocation - currentPct > 0.05) {
            parts.add(String.format("underweight in %s (%.0f%% vs %.0f%% target)",
                    category, currentPct * 100, targetAllocation * 100));
        }
        if (trendScore > 0) {
            parts.add("positive market momentum");
        }
        if (avgPersonalReturn != null && avgPersonalReturn > 0) {
            parts.add("category has performed well for you historically");
        }
        if (volatility > 0.4) {
            parts.add("note: high volatility");
        }
        return parts.isEmpty() ? "neutral signal" : String.join("; ", parts);
    }

    private String normalizeCategory(String category) {
        return (category == null || category.isBlank()) ? "Uncategorized" : category;
    }
}
