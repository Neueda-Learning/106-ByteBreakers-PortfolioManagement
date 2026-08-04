package com.neueda.portfolio.service;

import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.repository.InvestmentOptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvestmentOptionService {

    @Autowired
    private InvestmentOptionRepository investmentOptionRepository;

    public List<InvestmentOption> getAllInvestmentOptions() {
        return investmentOptionRepository.findAll();
    }

    public InvestmentOption getInvestmentOptionById(Long id) {
        InvestmentOption investmentOption = investmentOptionRepository.findById(id);
        if (investmentOption == null) {
            throw new RuntimeException("Investment option not found with id: " + id);
        }
        return investmentOption;
    }

    public int createInvestmentOption(InvestmentOption investmentOption) {
        return investmentOptionRepository.createInvestmentOption(investmentOption);
    }

    public int updateInvestmentOption(Long id, InvestmentOption investmentOption) {
        // Ensure a clear not-found error before attempting update.
        getInvestmentOptionById(id);

        int rowsAffected = investmentOptionRepository.updateInvestmentOption(id, investmentOption);
        if (rowsAffected == 0) {
            throw new RuntimeException("Investment option not found with id: " + id);
        }
        return rowsAffected;
    }

    public int deleteInvestmentOption(Long id) {
        // Ensure a clear not-found error before attempting delete.
        getInvestmentOptionById(id);

        int rowsAffected = investmentOptionRepository.deleteInvestmentOption(id);
        if (rowsAffected == 0) {
            throw new RuntimeException("Investment option not found with id: " + id);
        }
        return rowsAffected;
    }
}

