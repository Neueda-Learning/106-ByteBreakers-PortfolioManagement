package com.neueda.portfolio;

import com.neueda.portfolio.controller.InvestmentOptionController;
import com.neueda.portfolio.entity.InvestmentOption;
import com.neueda.portfolio.service.InvestmentOptionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class InvestmentOptionControllerTest {

	@Mock
	private InvestmentOptionService investmentOptionService;

	@InjectMocks
	private InvestmentOptionController investmentOptionController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(investmentOptionController).build();
	}

	@Test
	void getAllInvestmentOptionsReturnsAllAvailableOptions() throws Exception {
		when(investmentOptionService.getAllInvestmentOptions()).thenReturn(List.of(
				createInvestmentOption(1L, "Apple", "Technology", "189.25", "up", "12.50", "0.35"),
				createInvestmentOption(2L, "Microsoft", "Technology", "412.10", "strong_up", "10.75", "0.28")
		));

		mockMvc.perform(get("/investment-options/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("Apple")))
				.andExpect(jsonPath("$[0].currentPrice", is(189.25)))
				.andExpect(jsonPath("$[1].id", is(2)))
				.andExpect(jsonPath("$[1].name", is("Microsoft")))
				.andExpect(jsonPath("$[1].trend", is("strong_up")));
	}

	@Test
	void getAllInvestmentOptionsReturnsEmptyArrayWhenNoOptionsExist() throws Exception {
		when(investmentOptionService.getAllInvestmentOptions()).thenReturn(Collections.emptyList());

		mockMvc.perform(get("/investment-options/"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)));
	}

	@Test
	void getInvestmentOptionByIdReturnsRequestedOptionWhenItExists() throws Exception {
		when(investmentOptionService.getInvestmentOptionById(7L)).thenReturn(
				createInvestmentOption(7L, "NVIDIA", "Technology", "118.75", "strong_up", "18.40", "0.51")
		);

		mockMvc.perform(get("/investment-options/{id}", 7L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(7)))
				.andExpect(jsonPath("$.name", is("NVIDIA")))
				.andExpect(jsonPath("$.category", is("Technology")))
				.andExpect(jsonPath("$.estimatedReturn", is(18.40)))
				.andExpect(jsonPath("$.volatility", is(0.51)));
	}

	@Test
	void getInvestmentOptionByIdReturnsNotFoundWhenOptionDoesNotExist() throws Exception {
		when(investmentOptionService.getInvestmentOptionById(99L))
				.thenThrow(new RuntimeException("Investment option not found with id: 99"));

		mockMvc.perform(get("/investment-options/{id}", 99L))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));
	}

	@Test
	void createInvestmentOptionReturnsCreatedWhenPayloadIsValid() throws Exception {
		when(investmentOptionService.createInvestmentOption(any(InvestmentOption.class))).thenReturn(1);

		mockMvc.perform(post("/investment-options/")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "id": 3,
								  "name": "Tesla",
								  "category": "Automotive",
								  "currentPrice": 220.50,
								  "trend": "flat",
								  "estimatedReturn": 15.20,
								  "volatility": 0.48
								}
								"""))
				.andExpect(status().isCreated())
				.andExpect(content().string(""));
	}

	@Test
	void updateInvestmentOptionReturnsOkWhenOptionExists() throws Exception {
		when(investmentOptionService.updateInvestmentOption(eq(4L), any(InvestmentOption.class))).thenReturn(1);

		mockMvc.perform(put("/investment-options/{id}", 4L)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "id": 4,
								  "name": "Amazon",
								  "category": "Consumer",
								  "currentPrice": 145.30,
								  "trend": "down",
								  "estimatedReturn": 8.90,
								  "volatility": 0.32
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(content().string(""));
	}

	@Test
	void updateInvestmentOptionReturnsNotFoundWhenOptionDoesNotExist() throws Exception {
		when(investmentOptionService.updateInvestmentOption(eq(404L), any(InvestmentOption.class)))
				.thenThrow(new RuntimeException("Investment option not found with id: 404"));

		mockMvc.perform(put("/investment-options/{id}", 404L)
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "id": 404,
								  "name": "Missing",
								  "category": "Unknown",
								  "currentPrice": 0.00,
								  "trend": "flat",
								  "estimatedReturn": 0.00,
								  "volatility": 0.00
								}
								"""))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));
	}

	@Test
	void deleteInvestmentOptionReturnsNoContentWhenOptionExists() throws Exception {
		when(investmentOptionService.deleteInvestmentOption(5L)).thenReturn(1);

		mockMvc.perform(delete("/investment-options/{id}", 5L))
				.andExpect(status().isNoContent())
				.andExpect(content().string(""));
	}

	@Test
	void deleteInvestmentOptionReturnsNotFoundWhenOptionDoesNotExist() throws Exception {
		when(investmentOptionService.deleteInvestmentOption(505L))
				.thenThrow(new RuntimeException("Investment option not found with id: 505"));

		mockMvc.perform(delete("/investment-options/{id}", 505L))
				.andExpect(status().isNotFound())
				.andExpect(content().string(""));
	}

	private InvestmentOption createInvestmentOption(Long id,
													String name,
													String category,
													String currentPrice,
													String trend,
													String estimatedReturn,
													String volatility) {
		return new InvestmentOption(
				id,
				name,
				category,
				new BigDecimal(currentPrice),
				trend,
				new BigDecimal(estimatedReturn),
				new BigDecimal(volatility)
		);
	}
}

