package com.neueda.byteBreakers.portfolioManager.service;

import com.neueda.byteBreakers.portfolioManager.entity.UserInvestment;
import com.neueda.byteBreakers.portfolioManager.repository.UserInvestmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserInvestmentService
{
    @Autowired
    private UserInvestmentRepository userInvestmentRepository;

    public List<UserInvestment> getAllUserInvestments() {
        return userInvestmentRepository.getAllUserInvestments();
    }
}
