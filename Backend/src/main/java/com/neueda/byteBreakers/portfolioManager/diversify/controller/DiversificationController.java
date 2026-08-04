package com.neueda.byteBreakers.portfolioManager.diversify.controller;

;
import com.neueda.byteBreakers.portfolioManager.diversify.dto.SuggestionDTO;
import com.neueda.byteBreakers.portfolioManager.diversify.service.DiversificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/diversification")
public class DiversificationController {

    private final DiversificationService diversificationService;

    public DiversificationController(DiversificationService diversificationService) {
        this.diversificationService = diversificationService;
    }

    @GetMapping("/suggestions")
    public List<SuggestionDTO> getSuggestions(
            @RequestParam(defaultValue = "5") int topN) {
        return diversificationService.getSuggestions(topN);
    }
}
