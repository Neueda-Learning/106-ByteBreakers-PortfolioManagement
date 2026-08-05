package com.neueda.portfolio.diversify.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Maps to transaction_history -- append-only, never updated or deleted.
 * Used for history/audit views, NOT for computing current holdings
 * (that's current_holdings' job).
 */
public class History {

    private Long id;
    private Option option;
    private String action; // "BUY" or "SELL"
    private BigDecimal quantity;
    private BigDecimal boughtPrice; // price at the time of this transaction
    private LocalDate purchaseDate;

    public History() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Option getInvestmentOption() { return option; }
    public void setInvestmentOption(Option option) { this.option = option; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getBoughtPrice() { return boughtPrice; }
    public void setBoughtPrice(BigDecimal boughtPrice) { this.boughtPrice = boughtPrice; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
}
