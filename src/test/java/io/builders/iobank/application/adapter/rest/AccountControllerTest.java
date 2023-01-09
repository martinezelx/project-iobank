package io.builders.iobank.application.adapter.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.builders.iobank.application.adapter.rest.dto.AccountDto;
import io.builders.iobank.application.adapter.rest.mapper.AccountMapper;
import io.builders.iobank.domain.exceptions.AccountNotFoundException;
import io.builders.iobank.domain.model.Account;
import io.builders.iobank.domain.model.ProtocolType;
import io.builders.iobank.domain.model.User;
import io.builders.iobank.domain.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AccountService accountService;

    @MockBean
    private AccountMapper accountMapper;

    @Test
    void shouldCreateAccount() throws Exception {
        AccountDto accountDto = new AccountDto();
        accountDto.setId("123");
        accountDto.setBalance(BigDecimal.valueOf(100));
        accountDto.setProtocol(ProtocolType.BTC);
        accountDto.setUserId(1L);

        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        when(accountMapper.toDomain(accountDto)).thenReturn(account);
        when(accountService.saveAccount(any(Account.class))).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        mockMvc.perform(post("/api/v1/iobank/accounts/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is("123")))
                .andExpect(jsonPath("$.balance", is(100)))
                .andExpect(jsonPath("$.protocol", is("BTC")))
                .andExpect(jsonPath("$.userId", is(1)));
    }

    @Test
    void shouldGetAccounts() throws Exception {
        List<Account> accounts = new ArrayList<>();
        Account account1 = new Account();
        account1.setId("123");
        account1.setBalance(BigDecimal.valueOf(100));
        account1.setProtocol(ProtocolType.BTC);
        User user1 = new User();
        user1.setId(1L);
        account1.setUser(user1);
        Account account2 = new Account();
        account2.setId("456");
        account2.setBalance(BigDecimal.valueOf(200));
        account2.setProtocol(ProtocolType.BTC);
        User user2 = new User();
        user2.setId(2L);
        account2.setUser(user2);
        accounts.add(account1);
        accounts.add(account2);

        List<AccountDto> accountDtos = new ArrayList<>();
        AccountDto accountDto1 = new AccountDto();
        accountDto1.setId("123");
        accountDto1.setBalance(BigDecimal.valueOf(100));
        accountDto1.setProtocol(ProtocolType.BTC);
        accountDto1.setUserId(1L);
        AccountDto accountDto2 = new AccountDto();
        accountDto2.setId("456");
        accountDto2.setBalance(BigDecimal.valueOf(200));
        accountDto2.setProtocol(ProtocolType.BTC);
        accountDto2.setUserId(2L);
        accountDtos.add(accountDto1);
        accountDtos.add(accountDto2);

        when(accountService.getAccounts()).thenReturn(accounts);
        when(accountMapper.listToDto(accounts)).thenReturn(accountDtos);

        mockMvc.perform(get("/api/v1/iobank/accounts/search"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id", is("123")))
                .andExpect(jsonPath("$[0].balance", is(100)))
                .andExpect(jsonPath("$[0].protocol", is("BTC")))
                .andExpect(jsonPath("$[0].userId", is(1)))
                .andExpect(jsonPath("$[1].id", is("456")))
                .andExpect(jsonPath("$[1].balance", is(200)))
                .andExpect(jsonPath("$[1].protocol", is("BTC")))
                .andExpect(jsonPath("$[1].userId", is(2)));

        verify(accountService, times(1)).getAccounts();
        verify(accountMapper, times(1)).listToDto(accounts);
    }

    @Test
    void shouldGetAccountById() throws Exception {
        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        AccountDto accountDto = new AccountDto();
        accountDto.setId("123");
        accountDto.setBalance(BigDecimal.valueOf(100));
        accountDto.setProtocol(ProtocolType.BTC);
        accountDto.setUserId(1L);

        when(accountService.getAccount("123")).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        mockMvc.perform(get("/api/v1/iobank/accounts/search/123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("123"))
                .andExpect(jsonPath("$.balance").value(100))
                .andExpect(jsonPath("$.protocol", is("BTC")))
                .andExpect(jsonPath("$.userId", is(1)));

        verify(accountService, times(1)).getAccount("123");
        verify(accountMapper, times(1)).toDto(account);
    }

    @Test
    public void shouldReturnAccountNotFoundException() throws Exception {
        when(accountService.getAccount("123")).thenThrow(new AccountNotFoundException("Account not found"));

        mockMvc.perform(get("/api/v1/iobank/accounts/search/123"))
                .andExpect(status().isNotFound());

        verify(accountService, times(1)).getAccount("123");
    }

    @Test
    void shouldPutBalanceInAccount() throws Exception {
        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        AccountDto accountDto = new AccountDto();
        accountDto.setId("123");
        accountDto.setBalance(BigDecimal.valueOf(100));
        accountDto.setProtocol(ProtocolType.BTC);
        accountDto.setUserId(1L);

        BigDecimal newBalance = BigDecimal.valueOf(10);

        when(accountService.depositBalanceInAccount("123", newBalance)).thenReturn(account);
        when(accountMapper.toDto(account)).thenReturn(accountDto);

        mockMvc.perform(put("/api/v1/iobank/accounts/deposit/123")
                        .param("balance", String.valueOf(newBalance)))
                .andExpect(status().isOk());

        verify(accountService, times(1)).depositBalanceInAccount("123", newBalance);
        verify(accountMapper, times(1)).toDto(account);
    }
}