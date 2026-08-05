package com.neueda.portfolio;

import com.neueda.portfolio.dto.HoldingDetailsDTO;
import com.neueda.portfolio.dto.InvestmentDetailDTO;
import com.neueda.portfolio.entity.CurrentHolding;
import com.neueda.portfolio.repository.CurrentHoldingRepository;
import com.neueda.portfolio.service.CurrentHoldingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrentHoldingServiceTest {

    @Mock
    private CurrentHoldingRepository repository;

    @InjectMocks
    private CurrentHoldingService service;

    @Test
    void shouldCreateHoldingSuccessfully() {

        CurrentHolding holding = new CurrentHolding();

        when(repository.add(holding)).thenReturn(holding);

        CurrentHolding result = service.create(holding);

        assertNotNull(result);
        verify(repository).add(holding);
    }

    @Test
    void shouldReturnHoldingById() {

        CurrentHolding holding = new CurrentHolding();
        holding.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(holding));

        CurrentHolding result = service.getById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(repository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenHoldingIdNotFound() {

        when(repository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getById(1L)
        );

        assertEquals("Current holding not found", exception.getMessage());

        verify(repository).findById(1L);
    }

    @Test
    void shouldReturnHoldingByOptionId() {

        CurrentHolding holding = new CurrentHolding();
        holding.setOptionId(2L);

        when(repository.findByOptionId(2L))
                .thenReturn(Optional.of(holding));

        CurrentHolding result = service.getByOptionId(2L);

        assertNotNull(result);
        assertEquals(2L, result.getOptionId());

        verify(repository).findByOptionId(2L);
    }

    @Test
    void shouldThrowExceptionWhenOptionHoldingNotFound() {

        when(repository.findByOptionId(2L))
                .thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.getByOptionId(2L)
        );

        assertEquals(
                "Holding not found for option",
                exception.getMessage()
        );

        verify(repository).findByOptionId(2L);
    }

    @Test
    void shouldReturnAllHoldings() {

        List<CurrentHolding> holdings = List.of(
                new CurrentHolding(),
                new CurrentHolding()
        );

        when(repository.findAll()).thenReturn(holdings);

        List<CurrentHolding> result = service.getAll();

        assertEquals(2, result.size());

        verify(repository).findAll();
    }

    @Test
    void shouldReturnEmptyHoldingList() {

        when(repository.findAll()).thenReturn(List.of());

        List<CurrentHolding> result = service.getAll();

        assertTrue(result.isEmpty());

        verify(repository).findAll();
    }

    @Test
    void shouldUpdateHoldingForBuy() {

        CurrentHolding existing = new CurrentHolding();
        existing.setOptionId(1L);
        existing.setTotalQuantityOwned(new BigDecimal("10"));
        existing.setTotalInvested(new BigDecimal("1000"));

        CurrentHolding buy = new CurrentHolding();
        buy.setOptionId(1L);
        buy.setTotalQuantityOwned(new BigDecimal("5"));
        buy.setTotalInvested(new BigDecimal("500"));

        when(repository.findByOptionId(1L))
                .thenReturn(Optional.of(existing));

        service.update(buy, "BUY");

        assertEquals(
                0,
                existing.getTotalQuantityOwned()
                        .compareTo(new BigDecimal("15"))
        );

        assertEquals(
                0,
                existing.getTotalInvested()
                        .compareTo(new BigDecimal("1500"))
        );

        verify(repository).update(existing);
    }

    @Test
    void shouldUpdateHoldingForSell() {

        CurrentHolding existing = new CurrentHolding();
        existing.setOptionId(1L);
        existing.setTotalQuantityOwned(new BigDecimal("10"));
        existing.setTotalInvested(new BigDecimal("1000"));

        CurrentHolding sell = new CurrentHolding();
        sell.setOptionId(1L);
        sell.setTotalQuantityOwned(new BigDecimal("4"));
        sell.setTotalInvested(new BigDecimal("400"));

        when(repository.findByOptionId(1L))
                .thenReturn(Optional.of(existing));

        service.update(sell, "SELL");

        assertEquals(
                0,
                existing.getTotalQuantityOwned()
                        .compareTo(new BigDecimal("6"))
        );

        assertEquals(
                0,
                existing.getTotalInvested()
                        .compareTo(new BigDecimal("600"))
        );

        verify(repository).update(existing);
    }

    @Test
    void shouldThrowExceptionWhenSellingMoreThanOwned() {

        CurrentHolding existing = new CurrentHolding();
        existing.setOptionId(1L);
        existing.setTotalQuantityOwned(new BigDecimal("5"));
        existing.setTotalInvested(new BigDecimal("500"));

        CurrentHolding sell = new CurrentHolding();
        sell.setOptionId(1L);
        sell.setTotalQuantityOwned(new BigDecimal("10"));
        sell.setTotalInvested(new BigDecimal("1000"));

        when(repository.findByOptionId(1L))
                .thenReturn(Optional.of(existing));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> service.update(sell, "SELL")
        );

        assertEquals(
                "Cannot sell more than owned",
                exception.getMessage()
        );
    }

    @Test
    void shouldDeleteHolding() {

        service.delete(1L);

        verify(repository).deleteById(1L);
    }

    @Test
    void shouldReturnHoldingDetails() {

        HoldingDetailsDTO dto = new HoldingDetailsDTO();

        when(repository.getHoldingDetails(1L)).thenReturn(dto);

        HoldingDetailsDTO result = service.getHoldingDetails(1L);

        assertNotNull(result);

        verify(repository).getHoldingDetails(1L);
    }

    @Test
    void shouldReturnInvestmentDetails() {

        InvestmentDetailDTO dto = new InvestmentDetailDTO();

        when(repository.getInvestmentDetails(1L)).thenReturn(dto);

        InvestmentDetailDTO result = service.getInvestmentDetails(1L);

        assertNotNull(result);

        verify(repository).getInvestmentDetails(1L);
    }
}