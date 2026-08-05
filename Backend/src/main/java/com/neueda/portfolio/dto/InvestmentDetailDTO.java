package com.neueda.portfolio.dto;

import java.math.BigDecimal;

public class InvestmentDetailDTO {

    private Long id;
    private String name;
    private String category;
    private BigDecimal currentPrice;
    private String trend;
    private BigDecimal estimatedReturn;

    private BigDecimal quantityOwned;
    private BigDecimal totalInvested;
    private BigDecimal currentProfitLoss;

    public InvestmentDetailDTO(){}

    public InvestmentDetailDTO(Long id, String name, String category, BigDecimal currentPrice, String trend, BigDecimal estimatedReturn, BigDecimal quantityOwned, BigDecimal totalInvested, BigDecimal currentProfitLoss) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.trend = trend;
        this.estimatedReturn = estimatedReturn;
        this.quantityOwned = quantityOwned;
        this.totalInvested = totalInvested;
        this.currentProfitLoss = currentProfitLoss;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }

    public BigDecimal getEstimatedReturn() {
        return estimatedReturn;
    }

    public void setEstimatedReturn(BigDecimal estimatedReturn) {
        this.estimatedReturn = estimatedReturn;
    }

    public BigDecimal getQuantityOwned() {
        return quantityOwned;
    }

    public void setQuantityOwned(BigDecimal quantityOwned) {
        this.quantityOwned = quantityOwned;
    }

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
    }

    public BigDecimal getCurrentProfitLoss() {
        return currentProfitLoss;
    }

    public void setCurrentProfitLoss(BigDecimal currentProfitLoss) {
        this.currentProfitLoss = currentProfitLoss;
    }

}
