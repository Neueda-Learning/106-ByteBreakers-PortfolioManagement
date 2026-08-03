package com.example.diversify.service;

import com.example.diversify.dto.SuggestionDTO;
import com.example.diversify.model.InvestmentOption;
import com.example.diversify.model.UserInvestment;
import com.example.diversify.repository.InvestmentOptionRepository;
import com.example.diversify.repository.UserInvestmentRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class DiversificationService {

    private final InvestmentOptionRepository optionRepo;
    private final UserInvestmentRepository userInvestmentRepo;

    @Value("${diversify.weight.concentration:0.40}")
    private double weightConcentration;

    @Value("${diversify.weight.market:0.45}")
    private double weightMarket;

    @Value("${diversify.weight.personal:0.15}")
    private double weightPersonal;

    @Value("${diversify.volatility-penalty-strength:0.30}")
    private double volatilityPenaltyStrength;

    @Value("${diversify.overweight-multiplier:1.5}")
    private double overweightMultiplier; // category counted "overweight" past target * this

    public DiversificationService(InvestmentOptionRepository optionRepo,
                                   UserInvestmentRepository userInvestmentRepo) {
        this.optionRepo = optionRepo;
        this.userInvestmentRepo = userInvestmentRepo;
    }

    public List<SuggestionDTO> getSuggestions(int topN) {
        List<UserInvestment> holdings = userInvestmentRepo.findAllWithOption();
        List<InvestmentOption> allOptions = optionRepo.findAll();

        Map<String, Double> currentAllocationPct = computeCurrentAllocation(holdings);
        Map<String, Double> avgReturnByCategory = computePersonalReturns(holdings);
        Map<Long, Double> trendScoreByOption = computeTrendScores(allOptions);
        Map<Long, Double> rawMarketSignal = computeRawMarketSignal(allOptions, trendScoreByOption);
        Map<Long, Double> marketNorm = normalize(rawMarketSignal);
        Map<Long, Double> volatilityByOption = allOptions.stream()
                .collect(Collectors.toMap(InvestmentOption::getId, this::volatilityOrDefault));
        Map<Long, Double> volatilityNorm = normalize(volatilityByOption);

        Set<String> allCategories = new HashSet<>(currentAllocationPct.keySet());
        allOptions.forEach(o -> allCategories.add(o.getCategory()));
        double targetAllocation = allCategories.isEmpty() ? 0 : 1.0 / allCategories.size();

        List<SuggestionDTO> suggestions = new ArrayList<>();

        for (InvestmentOption option : allOptions) {
            String category = option.getCategory();
            double currentPct = currentAllocationPct.getOrDefault(category, 0.0);
            double concentrationGap = targetAllocation - currentPct; // positive = underweight
            boolean overweight = currentPct > targetAllocation * overweightMultiplier;

            double marketScore = marketNorm.getOrDefault(option.getId(), 0.5);

            double personalRaw = avgReturnByCategory.getOrDefault(category, 0.0);
            double personalClipped = Math.max(-0.5, Math.min(0.5, personalRaw));
            double personalScoreNorm = personalClipped + 0.5; // rescale [-0.5,0.5] -> [0,1]

            double rawScore =
                    weightConcentration * Math.max(concentrationGap, 0)
                    + weightMarket * marketScore
                    + weightPersonal * personalScoreNorm;

            double riskPenalty = volatilityPenaltyStrength * volatilityNorm.getOrDefault(option.getId(), 0.5);
            double finalScore = rawScore * (1 - riskPenalty);

            if (overweight) {
                finalScore *= 0.2; // deprioritize sharply, don't fully hide it
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
    }

    // -----------------------------------------------------------------
    // Signal computation -- each method mirrors one of the SQL queries
    // from the original design doc, but done in Java over fetched rows.
    // -----------------------------------------------------------------

    private Map<String, Double> computeCurrentAllocation(List<UserInvestment> holdings) {
        Map<String, Double> valueByCategory = new HashMap<>();
        double total = 0.0;
        for (UserInvestment ui : holdings) {
            double value = ui.getQuantity().doubleValue() * ui.getInvestmentOption().getCurrentPrice().doubleValue();
            String category = ui.getInvestmentOption().getCategory();
            valueByCategory.merge(category, value, Double::sum);
            total += value;
        }
        double finalTotal = total == 0 ? 1.0 : total;
        Map<String, Double> pctByCategory = new HashMap<>();
        valueByCategory.forEach((cat, val) -> pctByCategory.put(cat, val / finalTotal));
        return pctByCategory;
    }

    private Map<String, Double> computePersonalReturns(List<UserInvestment> holdings) {
        Map<String, List<Double>> returnsByCategory = new HashMap<>();
        for (UserInvestment ui : holdings) {
            double bought = ui.getBoughtPrice().doubleValue();
            double current = ui.getInvestmentOption().getCurrentPrice().doubleValue();
            if (bought == 0) continue;
            double unrealizedReturn = (current - bought) / bought;
            returnsByCategory.computeIfAbsent(ui.getInvestmentOption().getCategory(), k -> new ArrayList<>())
                    .add(unrealizedReturn);
        }
        Map<String, Double> avgByCategory = new HashMap<>();
        returnsByCategory.forEach((cat, list) ->
                avgByCategory.put(cat, list.stream().mapToDouble(Double::doubleValue).average().orElse(0.0)));
        return avgByCategory;
    }

    private Map<Long, Double> computeTrendScores(List<InvestmentOption> options) {
        Map<Long, Double> scores = new HashMap<>();
        for (InvestmentOption o : options) {
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

    private Map<Long, Double> computeRawMarketSignal(List<InvestmentOption> options, Map<Long, Double> trendScores) {
        Map<Long, Double> raw = new HashMap<>();
        for (InvestmentOption o : options) {
            double estReturn = o.getEstimatedReturn() != null ? o.getEstimatedReturn().doubleValue() : 0.0;
            double trendScore = trendScores.getOrDefault(o.getId(), 0.0);
            raw.put(o.getId(), 0.6 * trendScore + 0.4 * estReturn);
        }
        return raw;
    }

    private double volatilityOrDefault(InvestmentOption option) {
        return option.getVolatility() != null ? option.getVolatility().doubleValue() : 0.2;
    }

    /** Min-max normalize a map of raw values to 0-1. Flat input collapses to 0.5 for every key. */
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
}
