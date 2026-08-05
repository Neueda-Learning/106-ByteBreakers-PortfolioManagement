package com.neueda.portfolio;

import com.neueda.portfolio.controller.APIendpointController;
import com.neueda.portfolio.entity.CurrentHolding;
import com.neueda.portfolio.entity.TransactionHistory;
import com.neueda.portfolio.service.CurrentHoldingService;
import com.neueda.portfolio.service.TransactionHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class APIendpointControllerTest {

	@Mock
	private TransactionHistoryService transactionHistoryService;

	@Mock
	private CurrentHoldingService currentHoldingService;

	@InjectMocks
	private APIendpointController apIendpointController;

	private MockMvc mockMvc;

	@BeforeEach
	void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(apIendpointController).build();
	}

	@Test
	void buyReturnsOkAndUpdatesExistingHolding() throws Exception {
		when(currentHoldingService.getByOptionId(10L))
				.thenReturn(new CurrentHolding(10L, new BigDecimal("2.00"), new BigDecimal("100.00")));
		when(transactionHistoryService.createTransactionHistory(any(TransactionHistory.class))).thenReturn(1);

		mockMvc.perform(put("/api/v1/buy")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "optionId": 10,
								  "quantity": 3.50,
								  "currentPrice": 25.40,
								  "action": "sell"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(content().string(""));

		ArgumentCaptor<TransactionHistory> transactionCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
		verify(transactionHistoryService).createTransactionHistory(transactionCaptor.capture());
		assertEquals(Long.valueOf(10L), transactionCaptor.getValue().getInvestmentOptionId());
		assertEquals("BUY", transactionCaptor.getValue().getAction());
		assertEquals(new BigDecimal("3.50"), transactionCaptor.getValue().getQuantity());
		assertEquals(new BigDecimal("25.40"), transactionCaptor.getValue().getBoughtPrice());
		assertEquals(LocalDate.now(), transactionCaptor.getValue().getPurchaseDate());

		ArgumentCaptor<CurrentHolding> holdingCaptor = ArgumentCaptor.forClass(CurrentHolding.class);
		verify(currentHoldingService).update(holdingCaptor.capture(), org.mockito.ArgumentMatchers.eq("BUY"));
		assertEquals(Long.valueOf(10L), holdingCaptor.getValue().getOptionId());
		assertEquals(new BigDecimal("3.50"), holdingCaptor.getValue().getTotalQuantityOwned());
		assertEquals(new BigDecimal("88.9000"), holdingCaptor.getValue().getTotalInvested());
	}

	@Test
	void buyReturnsOkAndCreatesHoldingWhenOptionIsNotAlreadyOwned() throws Exception {
		when(currentHoldingService.getByOptionId(11L)).thenThrow(new RuntimeException("Holding not found for option"));
		when(transactionHistoryService.createTransactionHistory(any(TransactionHistory.class))).thenReturn(1);

		mockMvc.perform(put("/api/v1/buy")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "optionId": 11,
								  "quantity": 2.00,
								  "currentPrice": 40.50,
								  "action": "buy"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(content().string(""));

		ArgumentCaptor<CurrentHolding> holdingCaptor = ArgumentCaptor.forClass(CurrentHolding.class);
		verify(currentHoldingService).create(holdingCaptor.capture());
		assertEquals(Long.valueOf(11L), holdingCaptor.getValue().getOptionId());
		assertEquals(new BigDecimal("2.00"), holdingCaptor.getValue().getTotalQuantityOwned());
		assertEquals(new BigDecimal("81.0000"), holdingCaptor.getValue().getTotalInvested());
	}

	@Test
	void sellReturnsOkAndUpdatesExistingHolding() throws Exception {
		when(currentHoldingService.getByOptionId(12L))
				.thenReturn(new CurrentHolding(12L, new BigDecimal("5.00"), new BigDecimal("250.00")));
		when(transactionHistoryService.createTransactionHistory(any(TransactionHistory.class))).thenReturn(1);

		mockMvc.perform(put("/api/v1/sell")
						.contentType(MediaType.APPLICATION_JSON)
						.content("""
								{
								  "optionId": 12,
								  "quantity": 1.25,
								  "currentPrice": 60.00,
								  "action": "buy"
								}
								"""))
				.andExpect(status().isOk())
				.andExpect(content().string(""));

		ArgumentCaptor<TransactionHistory> transactionCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
		verify(transactionHistoryService).createTransactionHistory(transactionCaptor.capture());
		assertEquals(Long.valueOf(12L), transactionCaptor.getValue().getInvestmentOptionId());
		assertEquals("SELL", transactionCaptor.getValue().getAction());
		assertEquals(new BigDecimal("1.25"), transactionCaptor.getValue().getQuantity());
		assertEquals(new BigDecimal("60.00"), transactionCaptor.getValue().getBoughtPrice());

		ArgumentCaptor<CurrentHolding> holdingCaptor = ArgumentCaptor.forClass(CurrentHolding.class);
		verify(currentHoldingService).update(holdingCaptor.capture(), org.mockito.ArgumentMatchers.eq("SELL"));
		assertEquals(Long.valueOf(12L), holdingCaptor.getValue().getOptionId());
		assertEquals(new BigDecimal("1.25"), holdingCaptor.getValue().getTotalQuantityOwned());
		assertEquals(new BigDecimal("75.0000"), holdingCaptor.getValue().getTotalInvested());
	}

	@Test
	void sellThrowsWhenHoldingDoesNotExist() {
		when(currentHoldingService.getByOptionId(404L)).thenReturn(null);
		when(transactionHistoryService.createTransactionHistory(any(TransactionHistory.class))).thenReturn(1);

		Exception exception = assertThrows(Exception.class, () ->
				mockMvc.perform(put("/api/v1/sell")
								.contentType(MediaType.APPLICATION_JSON)
								.content("""
										{
										  "optionId": 404,
										  "quantity": 1.00,
										  "currentPrice": 12.50,
										  "action": "sell"
										}
										""")));

		Throwable cause = exception.getCause();
		RuntimeException runtimeException = assertInstanceOf(RuntimeException.class, cause);
		assertEquals("No holdings to sell for this option", runtimeException.getMessage());

		ArgumentCaptor<TransactionHistory> transactionCaptor = ArgumentCaptor.forClass(TransactionHistory.class);
		verify(transactionHistoryService).createTransactionHistory(transactionCaptor.capture());
		assertEquals(Long.valueOf(404L), transactionCaptor.getValue().getInvestmentOptionId());
		assertEquals("SELL", transactionCaptor.getValue().getAction());
	}
}
