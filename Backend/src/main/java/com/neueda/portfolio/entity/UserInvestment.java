package com.neueda.portfolio.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserInvestment {

    private Long id;
    private InvestmentOption investmentOption;
    private BigDecimal quantity;
    private BigDecimal boughtPrice;
    private LocalDate purchaseDate;

    public UserInvestment() {}

    public UserInvestment(Long id, InvestmentOption investmentOption, BigDecimal quantity,
                           BigDecimal boughtPrice, LocalDate purchaseDate) {
        this.id = id;
        this.investmentOption = investmentOption;
        this.quantity = quantity;
        this.boughtPrice = boughtPrice;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public InvestmentOption getInvestmentOption() { return investmentOption; }
    public void setInvestmentOption(InvestmentOption investmentOption) { this.investmentOption = investmentOption; }

    public BigDecimal getQuantity() { return quantity; }
    public void setQuantity(BigDecimal quantity) { this.quantity = quantity; }

    public BigDecimal getBoughtPrice() { return boughtPrice; }
    public void setBoughtPrice(BigDecimal boughtPrice) { this.boughtPrice = boughtPrice; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
}
