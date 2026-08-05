package com.neueda.portfolio.diversify.dto;

public class SuggestionDTO {

    private Long optionId;
    private String name;
    private String category;
    private double score;
    private String reason;

    public SuggestionDTO(Long optionId, String name, String category, double score, String reason) {
        this.optionId = optionId;
        this.name = name;
        this.category = category;
        this.score = score;
        this.reason = reason;
    }

    public Long getOptionId() { return optionId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getScore() { return score; }
    public String getReason() { return reason; }
}
