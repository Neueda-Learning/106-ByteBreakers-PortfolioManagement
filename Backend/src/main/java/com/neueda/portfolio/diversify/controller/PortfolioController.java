package com.neueda.portfolio.diversify.controller;

import com.neueda.portfolio.diversify.dto.SuggestionDTO;
import com.neueda.portfolio.diversify.dto.SummaryDTO;
import com.neueda.portfolio.diversify.service.DiversificationService;
import com.neueda.portfolio.diversify.service.SummaryService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    private final DiversificationService diversificationService;
    private final SummaryService summaryService;

    public PortfolioController(DiversificationService diversificationService,
                                SummaryService summaryService) {
        this.diversificationService = diversificationService;
        this.summaryService = summaryService;
    }

    @GetMapping("/diversify")
    public List<SuggestionDTO> getSuggestions(@RequestParam(defaultValue = "5") int topN) {
        return diversificationService.getSuggestions(topN);
    }

    @GetMapping("/summary")
    public SummaryDTO getSummary() {
        return summaryService.getSummary();
    }


}
