package io.builders.iobank.domain.service.impl;

import io.builders.iobank.domain.exceptions.AccountNotFoundException;
import io.builders.iobank.domain.model.Account;
import io.builders.iobank.domain.model.ProtocolType;
import io.builders.iobank.domain.model.User;
import io.builders.iobank.domain.port.repository.database.AccountRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class AccountServiceImplTest {

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private AccountRepository accountRepository;

    @Test
    void shouldGetAccountSuccess() {
        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        when(accountRepository.findById("123")).thenReturn(Optional.of(account));

        Account result = accountService.getAccount("123");

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals(new BigDecimal(100), result.getBalance());
        assertEquals(ProtocolType.BTC, result.getProtocol());
        assertEquals(user, result.getUser());

        verify(accountRepository, times(1)).findById("123");
    }

    @Test
    void shouldReturnAccountNotFoundException() {
        when(accountRepository.findById("123")).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccount("123"));

        verify(accountRepository, times(1)).findById("123");
    }

    @Test
    void shouldGetAccountsSuccess() {
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

        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> result = accountService.getAccounts();

        assertNotNull(result);
        assertEquals("123", result.get(0).getId());
        assertEquals(user1, result.get(0).getUser());
        assertEquals("456", result.get(1).getId());
        assertEquals(user2, result.get(1).getUser());

        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void shouldSaveAccountSuccess() {
        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.saveAccount(account);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals(new BigDecimal(100), result.getBalance());
        assertEquals(ProtocolType.BTC, result.getProtocol());
        assertEquals(user, result.getUser());

        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void shouldGetAccountByWalletAndProtocolSuccess() {
        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        when(accountRepository.findByIdAndProtocol("123", ProtocolType.BTC)).thenReturn(Optional.of(account));

        Account result = accountService.getAccountByWalletAndProtocol("123", ProtocolType.BTC);

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals(new BigDecimal(100), result.getBalance());
        assertEquals(ProtocolType.BTC, result.getProtocol());
        assertEquals(user, result.getUser());

        verify(accountRepository, times(1)).findByIdAndProtocol("123", ProtocolType.BTC);
    }

    @Test
    void shouldFindByIdAndProtocolReturnAccountNotFoundException() {
        when(accountRepository.findByIdAndProtocol("123456", ProtocolType.BTC))
                .thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, ()
                -> accountService.getAccountByWalletAndProtocol("123456", ProtocolType.BTC));

        verify(accountRepository, times(1)).findByIdAndProtocol("123456", ProtocolType.BTC);
    }

    @Test
    void shouldUpdateAccountSuccess() {
        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        Account result = accountService.updateAccount(account, new BigDecimal(500));

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals(new BigDecimal(500), result.getBalance());
        assertEquals(ProtocolType.BTC, result.getProtocol());
        assertEquals(user, result.getUser());

        verify(accountRepository, times(1)).save(account);
    }

    @Disabled("Disabled")
    @Test
    void shouldUpdateBalanceAccountSuccess() {
        Account account = new Account();
        account.setId("123");
        account.setBalance(BigDecimal.valueOf(100));
        account.setProtocol(ProtocolType.BTC);
        User user = new User();
        user.setId(1L);
        account.setUser(user);

        when(accountRepository.findById("123")).thenReturn(Optional.of(account));

        Account result = accountService.depositBalanceInAccount("123", new BigDecimal(500));

        assertNotNull(result);
        assertEquals("123", result.getId());
        assertEquals(new BigDecimal(600), result.getBalance());
        assertEquals(ProtocolType.BTC, result.getProtocol());
        assertEquals(user, result.getUser());

        verify(accountRepository, times(1)).save(account);
    }
}