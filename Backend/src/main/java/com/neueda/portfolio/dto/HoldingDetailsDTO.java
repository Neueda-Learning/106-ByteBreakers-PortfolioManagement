package com.neueda.portfolio.dto;

import java.math.BigDecimal;

public class HoldingDetailsDTO
{
    private BigDecimal quantityOwned;
    private BigDecimal totalInvested;
    private BigDecimal currentProfitLoss;

    public HoldingDetailsDTO(){}

    public HoldingDetailsDTO(BigDecimal quantityOwned, BigDecimal totalInvested, BigDecimal currentProfitLoss) {
        this.quantityOwned = quantityOwned;
        this.totalInvested = totalInvested;
        this.currentProfitLoss = currentProfitLoss;
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
