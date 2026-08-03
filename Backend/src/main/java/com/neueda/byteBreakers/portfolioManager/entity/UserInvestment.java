package com.neueda.byteBreakers.portfolioManager.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

public class UserInvestment {
    private Long id;
    private Long investmentOptionId;
    private BigDecimal quantity;
    private BigDecimal totalInvested;
    private LocalDate purchaseDate;

    public UserInvestment() {
    }

    public UserInvestment(Long investmentOptionId, BigDecimal quantity, BigDecimal totalInvested, LocalDate purchaseDate) {
        this.investmentOptionId = investmentOptionId;
        this.quantity = quantity;
        this.totalInvested = totalInvested;
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

    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
    }

    public LocalDate getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(LocalDate purchaseDate) {
        this.purchaseDate = purchaseDate;
    }


}
