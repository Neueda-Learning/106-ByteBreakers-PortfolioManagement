package com.neueda.byteBreakers.portfolioManager.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserInvestment {
    private Long id;
    private Long investmentOptionId;
    private BigDecimal quantity;
    private BigDecimal boughtPrice;
    private LocalDate purchaseDate;

    public UserInvestment() {
    }

    public UserInvestment(Long investmentOptionId, BigDecimal quantity, BigDecimal boughtPrice, LocalDate purchaseDate) {
        this.investmentOptionId = investmentOptionId;
        this.quantity = quantity;
        this.boughtPrice = boughtPrice;
        this.purchaseDate = purchaseDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInvestmentOptionId() {
        return investmentOptionId;
    }

    public void setInvestmentOptionId(Long investmentOptionId) {
        this.investmentOptionId = investmentOptionId;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getBoughtPrice() {
        return boughtPrice;
    }

    public void setBoughtPrice(BigDecimal boughtPrice) {
        this.boughtPrice = boughtPrice;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


}
