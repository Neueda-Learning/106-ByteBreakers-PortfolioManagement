package com.neueda.portfolio.dto;

import java.math.BigDecimal;

public class BuySellDTO
{
    private Long optionId;
    private BigDecimal quantity;
    private BigDecimal currentPrice;
    private String action; // "buy" or "sell"

    public BuySellDTO(Long optionId, BigDecimal quantity, BigDecimal currentPrice, String action) {
        this.optionId = optionId;
        this.quantity = quantity;
        this.currentPrice = currentPrice;
        this.action = action;
    }

    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
