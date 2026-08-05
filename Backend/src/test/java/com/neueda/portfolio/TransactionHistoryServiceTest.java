package com.neueda.portfolio;

import com.neueda.portfolio.entity.TransactionHistory;
import com.neueda.portfolio.repository.TransactionHistoryRepository;
import com.neueda.portfolio.service.TransactionHistoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionHistoryServiceTest {

    @Mock
    private TransactionHistoryRepository repository;

    @InjectMocks
    private TransactionHistoryService service;

    @Test
    void shouldReturnAllTransactionHistory() {
        TransactionHistory transaction1 = new TransactionHistory();
        transaction1.setId(1L);
        transaction1.setAction("BUY");

        TransactionHistory transaction2 = new TransactionHistory();
        transaction2.setId(2L);
        transaction2.setAction("SELL");

        when(repository.findAll()).thenReturn(List.of(transaction1, transaction2));

        List<TransactionHistory> result = service.getAllTransactionHistorys();

        assertEquals(2, result.size());
        assertEquals("BUY", result.get(0).getAction());
        assertEquals("SELL", result.get(1).getAction());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyTransactionHistoryList() {

        when(repository.findAll()).thenReturn(List.of());

        List<TransactionHistory> result = service.getAllTransactionHistorys();

        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void shouldCreateTransactionHistory() {

        TransactionHistory transaction = new TransactionHistory();

        when(repository.createTransactionHistory(transaction)).thenReturn(1);

        int result = service.createTransactionHistory(transaction);

        assertEquals(1, result);

        verify(repository).createTransactionHistory(transaction);
    }

    @Test
    void shouldReturnTransactionHistoryById() {

        TransactionHistory transaction = new TransactionHistory();
        transaction.setId(1L);

        when(repository.getTransactionHistoryById(1L))
                .thenReturn(transaction);

        TransactionHistory result = service.getTransactionHistoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());

        verify(repository).getTransactionHistoryById(1L);
    }

    @Test
    void shouldReturnNullWhenTransactionHistoryNotFound() {

        when(repository.getTransactionHistoryById(99L))
                .thenReturn(null);

        TransactionHistory result = service.getTransactionHistoryById(99L);

        assertNull(result);

        verify(repository).getTransactionHistoryById(99L);
    }

    @Test
    void shouldUpdateTransactionHistory() {

        TransactionHistory transaction = new TransactionHistory();

        when(repository.updateTransactionHistory(1L, transaction))
                .thenReturn(1);

        int result = service.updateTransactionHistory(1L, transaction);

        assertEquals(1, result);

        verify(repository).updateTransactionHistory(1L, transaction);
    }

    @Test
    void shouldReturnZeroWhenUpdateFails() {

        TransactionHistory transaction = new TransactionHistory();

        when(repository.updateTransactionHistory(1L, transaction))
                .thenReturn(0);

        int result = service.updateTransactionHistory(1L, transaction);

        assertEquals(0, result);

        verify(repository).updateTransactionHistory(1L, transaction);
    }

    @Test
    void shouldDeleteTransactionHistory() {

        when(repository.deleteTransactionHistory(1L))
                .thenReturn(1);

        int result = service.deleteTransactionHistory(1L);

        assertEquals(1, result);

        verify(repository).deleteTransactionHistory(1L);
    }

    @Test
    void shouldReturnZeroWhenDeleteFails() {

        when(repository.deleteTransactionHistory(1L))
                .thenReturn(0);

        int result = service.deleteTransactionHistory(1L);

        assertEquals(0, result);

        verify(repository).deleteTransactionHistory(1L);
    }
}