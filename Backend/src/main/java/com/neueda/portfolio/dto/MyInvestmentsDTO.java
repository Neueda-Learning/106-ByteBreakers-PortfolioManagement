package com.neueda.portfolio.dto;

import java.math.BigDecimal;

public class MyInvestmentsDTO
{
    private String name;
    private String category;
    private BigDecimal currentPrice;
    private BigDecimal totalQuantityOwned;
    private String trend;

    public MyInvestmentsDTO(String name, String category, BigDecimal currentPrice, BigDecimal totalQuantityOwned, String trend) {
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.totalQuantityOwned = totalQuantityOwned;
        this.trend = trend;
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

    public BigDecimal getTotalQuantityOwned() {
        return totalQuantityOwned;
    }

    public void setTotalQuantityOwned(BigDecimal totalQuantityOwned) {
        this.totalQuantityOwned = totalQuantityOwned;
    }

    public String getTrend() {
        return trend;
    }

    public void setTrend(String trend) {
        this.trend = trend;
    }
}
