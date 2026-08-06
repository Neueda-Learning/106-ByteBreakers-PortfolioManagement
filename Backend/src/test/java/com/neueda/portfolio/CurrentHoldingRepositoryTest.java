package com.neueda.portfolio;


import com.neueda.portfolio.dto.HoldingDetailsDTO;
import com.neueda.portfolio.dto.InvestmentDetailDTO;
import com.neueda.portfolio.entity.CurrentHolding;
import com.neueda.portfolio.repository.CurrentHoldingRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@DataJdbcTest
@Import(CurrentHoldingRepository.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CurrentHoldingRepositoryTest {
    @Autowired
    private CurrentHoldingRepository repository;

    @Test
    void shouldReturnAllCurrentHoldings() {

        List<CurrentHolding> holdings = repository.findAll();

        assertFalse(holdings.isEmpty());
    }

    @Test
    void shouldReturnHoldingById() {

        Optional<CurrentHolding> holding = repository.findById(1L);

        assertTrue(holding.isPresent());
    }

    @Test
    void shouldReturnHoldingByOptionId() {

        Optional<CurrentHolding> holding = repository.findByOptionId(1L);

        assertTrue(holding.isPresent());
    }

    @Test
    void shouldAddCurrentHolding() {

        CurrentHolding holding = new CurrentHolding();
        holding.setOptionId(4L);      // Safe optionId
        holding.setTotalQuantityOwned(new BigDecimal("10"));
        holding.setTotalInvested(new BigDecimal("1000"));

        repository.add(holding);

        Optional<CurrentHolding> savedHolding =
                repository.findByOptionId(4L);

        assertTrue(savedHolding.isPresent());

        assertEquals(
                0,
                savedHolding.get()
                        .getTotalQuantityOwned()
                        .compareTo(new BigDecimal("10"))
        );
    }

    @Test
    void shouldUpdateCurrentHolding() {

        Optional<CurrentHolding> optionalHolding = repository.findById(1L);

        assertTrue(optionalHolding.isPresent());

        CurrentHolding holding = optionalHolding.get();
        holding.setTotalInvested(new BigDecimal("9999"));

        repository.update(holding);

        Optional<CurrentHolding> updatedHolding = repository.findById(1L);

        assertTrue(updatedHolding.isPresent());
        assertEquals(
                0,
                updatedHolding.get()
                        .getTotalInvested()
                        .compareTo(new BigDecimal("9999"))
        );
    }

    @Test
    void shouldDeleteCurrentHolding() {

        repository.deleteById(1L);

        Optional<CurrentHolding> holding = repository.findById(1L);

        assertFalse(holding.isPresent());
    }

    @Test
    void shouldReturnHoldingDetails() {

        HoldingDetailsDTO dto = repository.getHoldingDetails(1L);

        assertNotNull(dto);
        assertNotNull(dto.getQuantityOwned());
        assertNotNull(dto.getTotalInvested());
        assertNotNull(dto.getCurrentProfitLoss());
    }

    @Test
    void shouldReturnInvestmentDetails() {

        InvestmentDetailDTO dto = repository.getInvestmentDetails(1L);

        assertNotNull(dto);

        assertEquals(1L, dto.getId());

        assertNotNull(dto.getName());
        assertNotNull(dto.getCategory());
        assertNotNull(dto.getCurrentPrice());
        assertNotNull(dto.getTrend());
        assertNotNull(dto.getEstimatedReturn());

        assertNotNull(dto.getQuantityOwned());
        assertNotNull(dto.getTotalInvested());
        assertNotNull(dto.getCurrentProfitLoss());
    }

}