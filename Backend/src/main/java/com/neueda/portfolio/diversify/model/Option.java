package com.neueda.portfolio.diversify.model;

import java.math.BigDecimal;

public class Option {

    private Long id;
    private String name;
    private String category;
    private BigDecimal currentPrice;
    private String trend;
    private BigDecimal estimatedReturn;
    private BigDecimal volatility;

    public Option() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public BigDecimal getCurrentPrice() { return currentPrice; }
    public void setCurrentPrice(BigDecimal currentPrice) { this.currentPrice = currentPrice; }

    public String getTrend() { return trend; }
    public void setTrend(String trend) { this.trend = trend; }

    public BigDecimal getEstimatedReturn() { return estimatedReturn; }
    public void setEstimatedReturn(BigDecimal estimatedReturn) { this.estimatedReturn = estimatedReturn; }

    public BigDecimal getVolatility() { return volatility; }
    public void setVolatility(BigDecimal volatility) { this.volatility = volatility; }
}
