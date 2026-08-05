package com.neueda.portfolio.diversify.service;

import com.neueda.portfolio.diversify.dto.RecentInvestmentDTO;
import com.neueda.portfolio.diversify.dto.SummaryDTO;
import com.neueda.portfolio.diversify.model.Holdin;
import com.neueda.portfolio.diversify.model.History;
import com.neueda.portfolio.diversify.repository.HoldingsRepository;
import com.neueda.portfolio.diversify.repository.HistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Totals/allocation come from current_holdings (already aggregated, one
 * row per stock -- no per-lot summing needed anymore). Recent activity
 * comes from transaction_history, since that's the only table that still
 * has individual buy/sell events with dates.
 */
@Service
public class SummaryService {
        @Autowired
    private HoldingsRepository currentHoldingsRepo;
        @Autowired
    private  HistoryRepository transactionHistoryRepo;

    @Value("${diversify.recent-investments-count:5}")
    private int recentInvestmentsCount;



    public SummaryDTO getSummary() {
        List<Holdin> holdings = currentHoldingsRepo.findAllWithOption();

        double totalInvestment = 0;
        double currentValue = 0;
        Map<String, Double> valueByCategory = new HashMap<>();

        for (Holdin h : holdings) {
            double qty = h.getTotalQuantityOwned().doubleValue();
            double invested = h.getTotalInvested().doubleValue();
            double current = qty * h.getInvestmentOption().getCurrentPrice().doubleValue();
            String category = h.getInvestmentOption().getCategory();

            totalInvestment += invested;
            currentValue += current;
            valueByCategory.merge(category, current, Double::sum);
        }

        // your formula: profit_loss = (qty * current_price) - total_invested,
        // summed across holdings -- same as current_value - total_investment overall
        double totalProfitLoss = currentValue - totalInvestment;
        double totalProfitLossPct = totalInvestment == 0 ? 0.0 : totalProfitLoss / totalInvestment;

        double safeCurrentValue = currentValue == 0 ? 1.0 : currentValue;
        Map<String, Double> allocationByCategory = new HashMap<>();
        valueByCategory.forEach((cat, val) -> allocationByCategory.put(cat, val / safeCurrentValue));

        List<History> recentTxns = transactionHistoryRepo.findRecent(recentInvestmentsCount);
        List<RecentInvestmentDTO> recent = recentTxns.stream()
                .map(t -> new RecentInvestmentDTO(
                        t.getInvestmentOption().getName(),
                        t.getInvestmentOption().getCategory(),
                        t.getAction(),
                        t.getQuantity(),
                        t.getBoughtPrice(),
                        t.getPurchaseDate()
                ))
                .collect(Collectors.toList());

        return new SummaryDTO(
                round2(totalInvestment),
                round2(currentValue),
                round2(totalProfitLoss),
                round4(totalProfitLossPct),
                allocationByCategory,
                recent
        );
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private double round4(double value) {
        return Math.round(value * 10000.0) / 10000.0;
    }
}
