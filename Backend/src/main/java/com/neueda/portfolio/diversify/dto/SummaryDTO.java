package com.neueda.portfolio.diversify.dto;

import java.util.List;
import java.util.Map;

public class SummaryDTO {

    private double totalInvestment;
    private double currentValue;
    private double totalProfitLoss;
    private double totalProfitLossPct;
    private Map<String, Double> allocationByCategory;
    private List<RecentInvestmentDTO> recentInvestments;

    public SummaryDTO(double totalInvestment, double currentValue, double totalProfitLoss,
                       double totalProfitLossPct, Map<String, Double> allocationByCategory,
                       List<RecentInvestmentDTO> recentInvestments) {
        this.totalInvestment = totalInvestment;
        this.currentValue = currentValue;
        this.totalProfitLoss = totalProfitLoss;
        this.totalProfitLossPct = totalProfitLossPct;
        this.allocationByCategory = allocationByCategory;
        this.recentInvestments = recentInvestments;
    }

    public double getTotalInvestment() { return totalInvestment; }
    public double getCurrentValue() { return currentValue; }
    public double getTotalProfitLoss() { return totalProfitLoss; }
    public double getTotalProfitLossPct() { return totalProfitLossPct; }
    public Map<String, Double> getAllocationByCategory() { return allocationByCategory; }
    public List<RecentInvestmentDTO> getRecentInvestments() { return recentInvestments; }
}
