package com.neueda.byteBreakers.portfolioManager.entity;

import java.math.BigDecimal;


public class InvestmentOption {

    private Long id;
    private String name;
    private String category;
    private BigDecimal currentPrice;
    private String trend; // strong_up | up | flat | down | strong_down
    private BigDecimal estimatedReturn;


    private BigDecimal volatility;

    public InvestmentOption() {}

    public InvestmentOption(Long id, String name, String category, BigDecimal currentPrice,
                             String trend, BigDecimal estimatedReturn, BigDecimal volatility) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.currentPrice = currentPrice;
        this.trend = trend;
        this.estimatedReturn = estimatedReturn;
        this.volatility = volatility;
    }

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
