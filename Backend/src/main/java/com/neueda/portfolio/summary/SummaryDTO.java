package com.neueda.portfolio.summary;

import java.util.List;
import java.util.Map;

public class SummaryDTO {

    private double totalInvestment;
    private double currentValue;
    private double profitLoss;
    private double profitLossPct;
    private Map<String, Double> allocationByCategory;
    private List<RecentInvestmentDTO> recentInvestments;

    public SummaryDTO(double totalInvestment,
                      double currentValue,
                      double profitLoss,
                      double profitLossPct,
                      Map<String, Double> allocationByCategory,List<RecentInvestmentDTO> recentInvestments) {

        this.totalInvestment = totalInvestment;
        this.currentValue = currentValue;
        this.profitLoss = profitLoss;
        this.profitLossPct = profitLossPct;
        this.allocationByCategory = allocationByCategory;
        this.recentInvestments = recentInvestments;
    }


    public double getTotalInvestment() {
        return totalInvestment;
    }

    public double getCurrentValue() {
        return currentValue;
    }

    public double getProfitLoss() {
        return profitLoss;
    }

    public double getProfitLossPct() {
        return profitLossPct;
    }

    public Map<String, Double> getAllocationByCategory() {
        return allocationByCategory;
    }
    public List<RecentInvestmentDTO> getRecentInvestments() {
        return recentInvestments;
    }
}
