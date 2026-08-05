package com.neueda.portfolio.controller;

import com.neueda.portfolio.dto.BuySellDTO;
import com.neueda.portfolio.dto.MyInvestmentsDTO;
import com.neueda.portfolio.entity.CurrentHolding;
import com.neueda.portfolio.entity.TransactionHistory;
import com.neueda.portfolio.service.CurrentHoldingService;
import com.neueda.portfolio.service.InvestmentOptionService;
import com.neueda.portfolio.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class APIendpointController
{
    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private CurrentHoldingService currentHoldingService;

    @Autowired
    private InvestmentOptionService investmentOptionService;

    @PutMapping("/buy")
    public void buy(@RequestBody BuySellDTO req) {
        TransactionHistory newObj = new TransactionHistory(req.getOptionId(), "BUY", req.getQuantity(), req.getCurrentPrice(), LocalDate.now());

        transactionHistoryService.createTransactionHistory(newObj);

        CurrentHolding newHolding = new CurrentHolding(req.getOptionId(), req.getQuantity(), req.getCurrentPrice().multiply(req.getQuantity()));

        try{
            if(currentHoldingService.getByOptionId(req.getOptionId()) != null) {

                currentHoldingService.update(newHolding, "BUY");
            }
        }
        catch(RuntimeException e){
            currentHoldingService.create(newHolding);
        }

    }

    @PutMapping("/sell")
    public void sell(@RequestBody BuySellDTO req) {
        TransactionHistory newObj = new TransactionHistory(req.getOptionId(), "SELL", req.getQuantity(), req.getCurrentPrice(), LocalDate.now());
        transactionHistoryService.createTransactionHistory(newObj);

        CurrentHolding newHolding = new CurrentHolding(req.getOptionId(), req.getQuantity(), req.getCurrentPrice().multiply(req.getQuantity()));
        if(currentHoldingService.getByOptionId(req.getOptionId()) != null) {
            currentHoldingService.update(newHolding, "SELL");
        } else {
            throw new RuntimeException("No holdings to sell for this option");
        }
    }

    @GetMapping("/investment")
    public List<MyInvestmentsDTO> getMyInvestments() {
        return investmentOptionService.getAllMyInvestments();
    }

}
