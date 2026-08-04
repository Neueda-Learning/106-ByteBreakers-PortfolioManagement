package com.neueda.portfolio.diversify.controller;

;
import com.neueda.portfolio.diversify.dto.SuggestionDTO;
import com.neueda.portfolio.diversify.service.DiversificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/diversification")
public class DiversificationController {

    @Autowired
    private DiversificationService diversificationService;
    @GetMapping("/suggestions")
    public List<SuggestionDTO> getSuggestions(
            @RequestParam(defaultValue = "5") int topN) {
        return diversificationService.getSuggestions(topN);
    }
    @GetMapping("/currentAllocation")
    public Map<String, Double> getCurrentAllocation() {
        return diversificationService.CurrentAllocation();
    }
}
