package com.neueda.byteBreakers.portfolioManager.controller;

import com.neueda.byteBreakers.portfolioManager.entity.UserInvestment;
import com.neueda.byteBreakers.portfolioManager.service.UserInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userInvestments")
@CrossOrigin("http://localhost:5173")
public class UserInvestmentController
{
    @Autowired
    private UserInvestmentService userInvestmentService;

    @GetMapping("/")
    public List<UserInvestment> getAllUserInvestments() {
        return userInvestmentService.getAllUserInvestments();
    }

}
