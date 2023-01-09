package io.builders.iobank.application.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.builders.iobank.application.adapter.rest.dto.TransactionDto;
import io.builders.iobank.application.adapter.rest.mapper.TransactionMapper;
import io.builders.iobank.domain.exceptions.TransactionNotFoundException;
import io.builders.iobank.domain.model.ProtocolType;
import io.builders.iobank.domain.model.Transaction;
import io.builders.iobank.domain.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @MockBean
    private TransactionMapper transactionMapper;

    @Test
    void shouldCreateTransaction() throws Exception {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setReference(uuid);
        transactionDto.setSource("account1");
        transactionDto.setDestination("account2");
        transactionDto.setAmount(BigDecimal.valueOf(100));
        transactionDto.setProtocol(ProtocolType.BTC);
        transactionDto.setDate(now);
        transactionDto.setFee(BigDecimal.valueOf(10));
        transactionDto.setDescription("Test transaction");

        Transaction transaction = new Transaction();
        transaction.setReference(uuid);
        transaction.setSource("account1");
        transaction.setDestination("account2");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setProtocol(ProtocolType.BTC);
        transaction.setDate(now);
        transaction.setFee(BigDecimal.valueOf(10));
        transaction.setDescription("Test transaction");

        when(transactionMapper.toDomain(transactionDto)).thenReturn(transaction);
        when(transactionService.createTransaction(any(Transaction.class))).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        mockMvc.perform(post("/api/v1/transactions/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(transactionDto)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldGetTransactions() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setReference(UUID.randomUUID());
        transaction1.setSource("account1");
        transaction1.setDestination("account2");
        transaction1.setAmount(BigDecimal.valueOf(100));
        transaction1.setProtocol(ProtocolType.BTC);
        transaction1.setDate(Instant.now());
        transaction1.setFee(BigDecimal.valueOf(10));
        transaction1.setDescription("Test transaction 1");
        transactions.add(transaction1);
        Transaction transaction2 = new Transaction();
        transaction2.setReference(UUID.randomUUID());
        transaction2.setSource("account2");
        transaction2.setDestination("account3");
        transaction2.setAmount(BigDecimal.valueOf(50));
        transaction2.setProtocol(ProtocolType.BTC);
        transaction2.setDate(Instant.now());
        transaction2.setFee(BigDecimal.valueOf(5));
        transaction2.setDescription("Test transaction 2");
        transactions.add(transaction2);

        List<TransactionDto> transactionDtos = new ArrayList<>();
        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setReference(transaction1.getReference());
        transactionDto1.setSource(transaction1.getSource());
        transactionDto1.setDestination(transaction1.getDestination());
        transactionDto1.setAmount(transaction1.getAmount());
        transactionDto1.setProtocol(transaction1.getProtocol());
        transactionDto1.setDate(transaction1.getDate());
        transactionDto1.setFee(transaction1.getFee());
        transactionDto1.setDescription(transaction1.getDescription());
        transactionDtos.add(transactionDto1);
        TransactionDto transactionDto2 = new TransactionDto();
        transactionDto2.setReference(transaction2.getReference());
        transactionDto2.setSource(transaction2.getSource());
        transactionDto2.setDestination(transaction2.getDestination());
        transactionDto2.setAmount(transaction2.getAmount());
        transactionDto2.setProtocol(transaction2.getProtocol());
        transactionDto2.setDate(transaction2.getDate());
        transactionDto2.setFee(transaction2.getFee());
        transactionDto2.setDescription(transaction2.getDescription());
        transactionDtos.add(transactionDto2);

        when(transactionService.getTransactions()).thenReturn(transactions);
        when(transactionMapper.listToDto(transactions)).thenReturn(transactionDtos);

        mockMvc.perform(get("/api/v1/transactions/search"))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).getTransactions();
        verify(transactionMapper, times(1)).listToDto(transactions);
    }

    @Test
    void shouldGetTransactionById() throws Exception {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setReference(uuid);
        transactionDto.setSource("account1");
        transactionDto.setDestination("account2");
        transactionDto.setAmount(BigDecimal.valueOf(100));
        transactionDto.setProtocol(ProtocolType.BTC);
        transactionDto.setDate(now);
        transactionDto.setFee(BigDecimal.valueOf(10));
        transactionDto.setDescription("Test transaction");

        Transaction transaction = new Transaction();
        transaction.setReference(uuid);
        transaction.setSource("account1");
        transaction.setDestination("account2");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setProtocol(ProtocolType.BTC);
        transaction.setDate(now);
        transaction.setFee(BigDecimal.valueOf(10));
        transaction.setDescription("Test transaction");

        when(transactionService.getTransaction(uuid)).thenReturn(transaction);
        when(transactionMapper.toDto(transaction)).thenReturn(transactionDto);

        mockMvc.perform(get("/api/v1/transactions/search/" + uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.source").value("account1"))
                .andExpect(jsonPath("$.destination").value("account2"));

        verify(transactionService, times(1)).getTransaction(uuid);
        verify(transactionMapper, times(1)).toDto(transaction);
    }

    @Test
    void shouldReturnTransactionNotFoundException() throws Exception {
        UUID uuid = UUID.randomUUID();

        when(transactionService.getTransaction(uuid)).thenThrow(new TransactionNotFoundException("Transaction not found"));

        mockMvc.perform(get("/api/v1/transactions/search/" + uuid))
                .andExpect(status().isNotFound());

        verify(transactionService, times(1)).getTransaction(uuid);
    }

    @Test
    void shouldGetMovements() throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        Transaction transaction1 = new Transaction();
        transaction1.setReference(UUID.randomUUID());
        transaction1.setSource("account1");
        transaction1.setDestination("account2");
        transaction1.setAmount(BigDecimal.valueOf(100));
        transaction1.setProtocol(ProtocolType.BTC);
        transaction1.setDate(Instant.now());
        transaction1.setFee(BigDecimal.valueOf(10));
        transaction1.setDescription("Test transaction 1");
        transactions.add(transaction1);
        Transaction transaction2 = new Transaction();
        transaction2.setReference(UUID.randomUUID());
        transaction2.setSource("account2");
        transaction2.setDestination("account3");
        transaction2.setAmount(BigDecimal.valueOf(50));
        transaction2.setProtocol(ProtocolType.BTC);
        transaction2.setDate(Instant.now());
        transaction2.setFee(BigDecimal.valueOf(5));
        transaction2.setDescription("Test transaction 2");
        transactions.add(transaction2);

        List<TransactionDto> transactionDtos = new ArrayList<>();
        TransactionDto transactionDto1 = new TransactionDto();
        transactionDto1.setReference(transaction1.getReference());
        transactionDto1.setSource(transaction1.getSource());
        transactionDto1.setDestination(transaction1.getDestination());
        transactionDto1.setAmount(transaction1.getAmount());
        transactionDto1.setProtocol(transaction1.getProtocol());
        transactionDto1.setDate(transaction1.getDate());
        transactionDto1.setFee(transaction1.getFee());
        transactionDto1.setDescription(transaction1.getDescription());
        transactionDtos.add(transactionDto1);
        TransactionDto transactionDto2 = new TransactionDto();
        transactionDto2.setReference(transaction2.getReference());
        transactionDto2.setSource(transaction2.getSource());
        transactionDto2.setDestination(transaction2.getDestination());
        transactionDto2.setAmount(transaction2.getAmount());
        transactionDto2.setProtocol(transaction2.getProtocol());
        transactionDto2.setDate(transaction2.getDate());
        transactionDto2.setFee(transaction2.getFee());
        transactionDto2.setDescription(transaction2.getDescription());
        transactionDtos.add(transactionDto2);

        when(transactionService.getMovements("account2")).thenReturn(transactions);
        when(transactionMapper.listToDto(transactions)).thenReturn(transactionDtos);

        mockMvc.perform(get("/api/v1/transactions/movements/" + "account2"))
                .andExpect(status().isOk());

        verify(transactionService, times(1)).getMovements("account2");
        verify(transactionMapper, times(1)).listToDto(transactions);
    }
}