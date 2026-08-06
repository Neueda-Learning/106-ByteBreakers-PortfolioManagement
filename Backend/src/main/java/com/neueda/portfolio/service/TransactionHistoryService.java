package com.neueda.portfolio.service;

import com.neueda.portfolio.repository.TransactionHistoryRepository;
import com.neueda.portfolio.entity.TransactionHistory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryService
{
    @Autowired
    private TransactionHistoryRepository transactionHistoryRepository;

    public List<TransactionHistory> getAllTransactionHistorys() {
        return transactionHistoryRepository.findAll();
    }

    public int createTransactionHistory(TransactionHistory transactionHistory) {
        return transactionHistoryRepository.createTransactionHistory(transactionHistory);
    }

    public TransactionHistory getTransactionHistoryById(Long id) {
        return transactionHistoryRepository.getTransactionHistoryById(id);
    }

    public int updateTransactionHistory(Long id, TransactionHistory transactionHistory) {
        return transactionHistoryRepository.updateTransactionHistory(id, transactionHistory);
    }

    public int deleteTransactionHistory(Long id) {
        return transactionHistoryRepository.deleteTransactionHistory(id);
    }

}
