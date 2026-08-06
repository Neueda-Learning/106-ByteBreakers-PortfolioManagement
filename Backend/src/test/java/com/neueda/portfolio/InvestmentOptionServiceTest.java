package com.neueda.portfolio;

import com.neueda.portfolio.dto.MyInvestmentsDTO;
import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.repository.InvestmentOptionRepository;
import com.neueda.portfolio.service.InvestmentOptionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InvestmentOptionServiceTest {

    @Mock
    private InvestmentOptionRepository repository;

    @InjectMocks
    private InvestmentOptionService service;

    @Test
    void shouldReturnAllInvestmentOptions() {

        List<InvestmentOption> investments = List.of(
                new InvestmentOption(1L, "Apple", "Stock",
                        new BigDecimal("150"), "UP",
                        new BigDecimal("12"), new BigDecimal("5")),
                new InvestmentOption(2L, "Google", "Stock",
                        new BigDecimal("250"), "DOWN",
                        new BigDecimal("10"), new BigDecimal("4"))
        );

        when(repository.findAll()).thenReturn(investments);

        List<InvestmentOption> result = service.getAllInvestmentOptions();

        assertEquals(2, result.size());
        assertEquals("Apple", result.get(0).getName());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyInvestmentOptionList() {

        when(repository.findAll()).thenReturn(List.of());

        List<InvestmentOption> result = service.getAllInvestmentOptions();

        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnInvestmentOptionById() {

        InvestmentOption investment = new InvestmentOption();
        investment.setId(1L);
        investment.setName("Apple");

        when(repository.findById(1L)).thenReturn(investment);

        InvestmentOption result = service.getInvestmentOptionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Apple", result.getName());

        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenInvestmentOptionNotFound() {

        when(repository.findById(99L)).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getInvestmentOptionById(99L)
        );

        assertEquals(
                "Investment option not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(99L);
    }

    @Test
    void shouldReturnAllMyInvestments() {

        List<MyInvestmentsDTO> investments = List.of(
                new MyInvestmentsDTO(
                        "Apple",
                        "Stock",
                        new BigDecimal("150"),
                        new BigDecimal("10"),
                        "UP"
                )
        );

        when(repository.findAllMyInvestments()).thenReturn(investments);

        List<MyInvestmentsDTO> result = service.getAllMyInvestments();

        assertEquals(1, result.size());
        assertEquals("Apple", result.get(0).getName());

        verify(repository).findAllMyInvestments();
    }

    @Test
    void shouldCreateInvestmentOption() {

        InvestmentOption investment = new InvestmentOption();

        when(repository.createInvestmentOption(investment)).thenReturn(1);

        int result = service.createInvestmentOption(investment);

        assertEquals(1, result);

        verify(repository).createInvestmentOption(investment);
    }

    @Test
    void shouldUpdateInvestmentOption() {

        InvestmentOption investment = new InvestmentOption();

        when(repository.findById(1L)).thenReturn(investment);
        when(repository.updateInvestmentOption(1L, investment)).thenReturn(1);

        int result = service.updateInvestmentOption(1L, investment);

        assertEquals(1, result);

        verify(repository).findById(1L);
        verify(repository).updateInvestmentOption(1L, investment);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingInvestmentOption() {

        InvestmentOption investment = new InvestmentOption();

        when(repository.findById(99L)).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.updateInvestmentOption(99L, investment)
        );

        assertEquals(
                "Investment option not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(99L);
        verify(repository, never()).updateInvestmentOption(anyLong(), any());
    }

    @Test
    void shouldDeleteInvestmentOption() {

        InvestmentOption investment = new InvestmentOption();

        when(repository.findById(1L)).thenReturn(investment);
        when(repository.deleteInvestmentOption(1L)).thenReturn(1);

        int result = service.deleteInvestmentOption(1L);

        assertEquals(1, result);

        verify(repository).findById(1L);
        verify(repository).deleteInvestmentOption(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistingInvestmentOption() {

        when(repository.findById(99L)).thenReturn(null);

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.deleteInvestmentOption(99L)
        );

        assertEquals(
                "Investment option not found with id: 99",
                exception.getMessage()
        );

        verify(repository).findById(99L);
        verify(repository, never()).deleteInvestmentOption(anyLong());
    }
}