package com.neueda.portfolio.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionHistory {

    private Long id;
    private Long investmentOptionId;
    private String action;
    private BigDecimal quantity;
    private BigDecimal boughtPrice;
    private LocalDate purchaseDate;

    public TransactionHistory() {}

    public TransactionHistory( Long investmentOption, String action, BigDecimal quantity,
                           BigDecimal boughtPrice, LocalDate purchaseDate) {
        this.investmentOptionId = investmentOption;
        this.action = action;
        this.quantity = quantity;
        this.boughtPrice = boughtPrice;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getInvestmentOptionId() { return investmentOptionId; }
    public void setInvestmentOptionId(Long investmentOptionId) { this.investmentOptionId = investmentOptionId; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getBoughtPrice() { return boughtPrice; }
    public void setBoughtPrice(BigDecimal boughtPrice) { this.boughtPrice = boughtPrice; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
