package com.neueda.portfolio;

import com.neueda.portfolio.dto.MyInvestmentsDTO;
import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.repository.InvestmentOptionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jdbc.test.autoconfigure.DataJdbcTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJdbcTest
@Import(InvestmentOptionRepository.class)
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class InvestmentOptionRepositoryTest {

    @Autowired
    private InvestmentOptionRepository repository;

    @Test
    void shouldReturnAllInvestmentOptions() {

        List<InvestmentOption> investments = repository.findAll();

        assertFalse(investments.isEmpty());
    }

    @Test
    void shouldReturnInvestmentById() {

        InvestmentOption investment = repository.findById(1L);

        assertNotNull(investment);
        assertEquals(1L, investment.getId());
    }

    @Test
    void shouldReturnNullWhenInvestmentNotFound() {

        InvestmentOption investment = repository.findById(999L);

        assertNull(investment);
    }

    @Test
    void shouldReturnAllMyInvestments() {

        List<MyInvestmentsDTO> investments = repository.findAllMyInvestments();

        assertFalse(investments.isEmpty());
    }

    @Test
    void shouldCreateInvestmentOption() {

        InvestmentOption investment = new InvestmentOption();

        investment.setName("JUnit Test Investment");
        investment.setCategory("EQUITY");
        investment.setCurrentPrice(new BigDecimal("500"));
        investment.setTrend("UP");
        investment.setEstimatedReturn(new BigDecimal("10"));
        investment.setVolatility(new BigDecimal("15"));

        int rows = repository.createInvestmentOption(investment);

        assertEquals(1, rows);
    }

    @Test
    void shouldUpdateInvestmentOption() {

        InvestmentOption investment = repository.findById(1L);

        assertNotNull(investment);

        investment.setName("Updated Investment");

        int rows = repository.updateInvestmentOption(1L, investment);

        assertEquals(1, rows);

        InvestmentOption updated = repository.findById(1L);

        assertEquals("Updated Investment", updated.getName());

        // Restore original value
        investment.setName("Reliance Industries");
        repository.updateInvestmentOption(1L, investment);
    }

    @Test
    void shouldDeleteInvestmentOption() {

        InvestmentOption investment = new InvestmentOption();

        investment.setName("Delete Test");
        investment.setCategory("EQUITY");
        investment.setCurrentPrice(new BigDecimal("100"));
        investment.setTrend("UP");
        investment.setEstimatedReturn(new BigDecimal("10"));
        investment.setVolatility(new BigDecimal("15"));

        repository.createInvestmentOption(investment);

        List<InvestmentOption> investments = repository.findAll();

        Long id = investments.get(investments.size() - 1).getId();

        int rows = repository.deleteInvestmentOption(id);

        assertEquals(1, rows);

        assertNull(repository.findById(id));
    }
}
