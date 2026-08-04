package com.neueda.portfolio.service;

import com.neueda.portfolio.repository.UserInvestmentRepository;
import com.neueda.portfolio.entity.UserInvestment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInvestmentService
{
    @Autowired
    private UserInvestmentRepository userInvestmentRepository;

    public List<UserInvestment> getAllUserInvestments() {
        return userInvestmentRepository.findAllWithOption();
    }
}
