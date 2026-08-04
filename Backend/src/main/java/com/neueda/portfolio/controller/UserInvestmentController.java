package com.neueda.portfolio.controller;

import com.neueda.portfolio.entity.UserInvestment;
import com.neueda.portfolio.service.UserInvestmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/userInvestments")
public class UserInvestmentController
{
    @Autowired
    private UserInvestmentService userInvestmentService;

    @GetMapping("/")
    public List<UserInvestment> getAllUserInvestments() {
        return userInvestmentService.getAllUserInvestments();
    }

}
