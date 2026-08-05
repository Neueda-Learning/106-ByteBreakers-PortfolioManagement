package com.neueda.portfolio;

import com.neueda.portfolio.entity.TransactionHistory;
import com.neueda.portfolio.repository.TransactionHistoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import(TransactionHistoryRepository.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class TransactionHistoryRepositoryTest {

    @Autowired
    private TransactionHistoryRepository repository;

    @Test
    void shouldReturnAllTransactionHistory() {

        List<TransactionHistory> transactions = repository.findAll();

        assertFalse(transactions.isEmpty());
    }

    @Test
    void shouldReturnTransactionHistoryById() {

        TransactionHistory transaction = repository.getTransactionHistoryById(1L);

        assertNotNull(transaction);
        assertEquals(1L, transaction.getId());
    }

    @Test
    void shouldReturnNullWhenTransactionHistoryNotFound() {

        TransactionHistory transaction = repository.getTransactionHistoryById(999L);

        assertNull(transaction);
    }

    @Test
    void shouldCreateTransactionHistory() {

        TransactionHistory transaction = new TransactionHistory();

        transaction.setInvestmentOptionId(4L);
        transaction.setAction("BUY");
        transaction.setQuantity(new BigDecimal("5"));
        transaction.setBoughtPrice(new BigDecimal("1000"));
        transaction.setPurchaseDate(LocalDate.now());

        int rows = repository.createTransactionHistory(transaction);

        assertEquals(1, rows);
    }

    @Test
    void shouldUpdateTransactionHistory() {

        TransactionHistory transaction = repository.getTransactionHistoryById(1L);

        assertNotNull(transaction);

        transaction.setBoughtPrice(new BigDecimal("9999"));

        int rows = repository.updateTransactionHistory(1L, transaction);

        assertEquals(1, rows);

        TransactionHistory updated = repository.getTransactionHistoryById(1L);

        assertEquals(
                0,
                updated.getBoughtPrice().compareTo(new BigDecimal("9999"))
        );
    }

    @Test
    void shouldDeleteTransactionHistory() {

        TransactionHistory transaction = new TransactionHistory();

        transaction.setInvestmentOptionId(4L);
        transaction.setAction("BUY");
        transaction.setQuantity(new BigDecimal("2"));
        transaction.setBoughtPrice(new BigDecimal("500"));
        transaction.setPurchaseDate(LocalDate.now());

        repository.createTransactionHistory(transaction);

        List<TransactionHistory> transactions = repository.findAll();

        Long id = transactions.get(transactions.size() - 1).getId();

        int rows = repository.deleteTransactionHistory(id);

        assertEquals(1, rows);

        assertNull(repository.getTransactionHistoryById(id));
    }
}