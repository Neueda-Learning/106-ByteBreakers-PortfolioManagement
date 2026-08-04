package com.neueda.portfolio.summary;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecentInvestmentDTO {

    private String name;
    private String category;
    private BigDecimal quantity;
    private BigDecimal boughtPrice;
    private LocalDate purchaseDate;

    public RecentInvestmentDTO(String name, String category, BigDecimal quantity,
                               BigDecimal boughtPrice, LocalDate purchaseDate) {
        this.name = name;
        this.category = category;
        this.quantity = quantity;
        this.boughtPrice = boughtPrice;
        this.purchaseDate = purchaseDate;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getBoughtPrice() { return boughtPrice; }
    public LocalDate getPurchaseDate() { return purchaseDate; }
}
