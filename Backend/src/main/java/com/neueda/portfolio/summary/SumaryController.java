package com.neueda.portfolio.summary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
 @RequestMapping("/summary")

public class SumaryController {
    @Autowired
    private Summaryservice summaryservice;
    @GetMapping("/")
    public SummaryDTO getSummary() {
        return summaryservice.getSummary();
    }

}
