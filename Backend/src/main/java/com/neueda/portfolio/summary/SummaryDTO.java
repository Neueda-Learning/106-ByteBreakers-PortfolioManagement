package com.neueda.portfolio.summary;

import java.util.Map;

public class SummaryDTO {

    private double totalInvestment;
    private double currentValue;
    private double profitLoss;
    private double profitLossPct;
    private Map<String, Double> allocationByCategory;

    public SummaryDTO(double totalInvestment,
                      double currentValue,
                      double profitLoss,
                      double profitLossPct,
                      Map<String, Double> allocationByCategory) {

        this.totalInvestment = totalInvestment;
        this.currentValue = currentValue;
        this.profitLoss = profitLoss;
        this.profitLossPct = profitLossPct;
        this.allocationByCategory = allocationByCategory;
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
}