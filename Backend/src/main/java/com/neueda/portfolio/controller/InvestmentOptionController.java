package com.neueda.portfolio.controller;

import com.neueda.portfolio.dto.HoldingDetailsDTO;
import com.neueda.portfolio.dto.InvestmentDetailDTO;
import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.service.CurrentHoldingService;
import com.neueda.portfolio.service.InvestmentOptionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/investment-options")
@CrossOrigin(origins = "*")
public class InvestmentOptionController {

    @Autowired
    private InvestmentOptionService investmentOptionService;

    @Autowired
    private CurrentHoldingService currentHoldingService;

    @GetMapping("/")
    public ResponseEntity<List<InvestmentOption>> getAllInvestmentOptions() {
        return ResponseEntity.ok(investmentOptionService.getAllInvestmentOptions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvestmentOption> getInvestmentOptionById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(investmentOptionService.getInvestmentOptionById(id));
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/holdings")
    public ResponseEntity<InvestmentDetailDTO> getHoldingDetails(@PathVariable Long id){
        return ResponseEntity.ok(currentHoldingService.getInvestmentDetails(id));
    }


    @PostMapping("/")
    public ResponseEntity<Void> createInvestmentOption(@Valid @RequestBody InvestmentOption investmentOption) {
        investmentOptionService.createInvestmentOption(investmentOption);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateInvestmentOption(@PathVariable Long id,
                                                        @Valid @RequestBody InvestmentOption investmentOption) {
        try {
            investmentOptionService.updateInvestmentOption(id, investmentOption);
            return ResponseEntity.ok().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestmentOption(@PathVariable Long id) {
        try {
            investmentOptionService.deleteInvestmentOption(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}

