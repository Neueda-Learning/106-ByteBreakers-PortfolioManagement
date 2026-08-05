package com.neueda.portfolio.controller;

import com.neueda.portfolio.dto.BuySellDTO;
import com.neueda.portfolio.entity.TransactionHistory;
import com.neueda.portfolio.service.CurrentHoldingService;
import com.neueda.portfolio.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1")
public class APIendpointController
{
    @Autowired
    private TransactionHistoryService transactionHistoryService;

    @Autowired
    private CurrentHoldingService currentHoldingService;

    @PutMapping("/buy")
    public void buy(@RequestBody BuySellDTO req) {
        TransactionHistory newObj = new TransactionHistory(req.getOptionId(), "BUY", req.getQuantity(), req.getCurrentPrice(), LocalDate.now());
        transactionHistoryService.createTransactionHistory();
        currentHoldingService.updateCurrentHoldings();
    }

}
