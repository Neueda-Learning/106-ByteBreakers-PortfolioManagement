package com.neueda.portfolio.diversify.model;

import java.math.BigDecimal;

/**
 * Maps to current_holdings -- one row per stock, kept in sync on every
 * buy/sell. This is what DiversificationService and SummaryService read
 * from now, NOT transaction_history (which is the append-only log).
 */
public class Holdin {

    private Long id;
    private Option option;
    private BigDecimal totalQuantityOwned;
    private BigDecimal totalInvested;

    public Holdin() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Option getInvestmentOption() { return option; }
    public void setInvestmentOption(Option option) { this.option = option; }

    public BigDecimal getTotalQuantityOwned() { return totalQuantityOwned; }
    public void setTotalQuantityOwned(BigDecimal totalQuantityOwned) { this.totalQuantityOwned = totalQuantityOwned; }

    public BigDecimal getTotalInvested() { return totalInvested; }
    public void setTotalInvested(BigDecimal totalInvested) { this.totalInvested = totalInvested; }
}
