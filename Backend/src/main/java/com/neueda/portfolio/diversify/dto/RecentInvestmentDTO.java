package com.neueda.portfolio.diversify.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class RecentInvestmentDTO {

    private String name;
    private String category;
    private String action;
    private BigDecimal quantity;
    private BigDecimal price;
    private LocalDate date;

    public RecentInvestmentDTO(String name, String category, String action,
                                BigDecimal quantity, BigDecimal price, LocalDate date) {
        this.name = name;
        this.category = category;
        this.action = action;
        this.quantity = quantity;
        this.price = price;
        this.date = date;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public String getAction() { return action; }
    public BigDecimal getQuantity() { return quantity; }
    public BigDecimal getPrice() { return price; }
    public LocalDate getDate() { return date; }
}
