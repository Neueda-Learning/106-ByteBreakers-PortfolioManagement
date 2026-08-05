package com.neueda.portfolio.entity;

import java.math.BigDecimal;

public class CurrentHolding {

    private Long id;

    private Long optionId;

    private BigDecimal totalQuantityOwned;

    private BigDecimal totalInvested;


    public CurrentHolding() {
    }

    public CurrentHolding(Long optionId,
                          BigDecimal totalQuantityOwned,
                          BigDecimal totalInvested) {
        this.optionId = optionId;
        this.totalQuantityOwned = totalQuantityOwned;
        this.totalInvested = totalInvested;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getOptionId() {
        return optionId;
    }

    public void setOptionId(Long optionId) {
        this.optionId = optionId;
    }


    public BigDecimal getTotalQuantityOwned() {
        return totalQuantityOwned;
    }

    public void setTotalQuantityOwned(BigDecimal totalQuantityOwned) {
        this.totalQuantityOwned = totalQuantityOwned;
    }


    public BigDecimal getTotalInvested() {
        return totalInvested;
    }

    public void setTotalInvested(BigDecimal totalInvested) {
        this.totalInvested = totalInvested;
    }
}
