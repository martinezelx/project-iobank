package io.builders.iobank.domain.service.impl;

import io.builders.iobank.domain.exceptions.AccountBalanceNotEnoughException;
import io.builders.iobank.domain.exceptions.AccountOverdrawnException;
import io.builders.iobank.domain.exceptions.TransactionNotFoundException;
import io.builders.iobank.domain.model.Account;
import io.builders.iobank.domain.model.ProtocolType;
import io.builders.iobank.domain.model.Transaction;
import io.builders.iobank.domain.model.User;
import io.builders.iobank.domain.port.repository.database.TransactionRepository;
import io.builders.iobank.domain.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private AccountService accountService;

    @Test
    void shouldGetTransactionSuccess() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        Transaction transaction = new Transaction();
        transaction.setReference(uuid);
        transaction.setSource("account1");
        transaction.setDestination("account2");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setProtocol(ProtocolType.BTC);
        transaction.setDate(now);
        transaction.setFee(BigDecimal.valueOf(10));
        transaction.setDescription("Test transaction");

        when(transactionRepository.findById(uuid)).thenReturn(Optional.of(transaction));

        Transaction result = transactionService.getTransaction(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getReference());
        assertEquals(new BigDecimal(100), result.getAmount());
        assertEquals(now, result.getDate());

        verify(transactionRepository, times(1)).findById(uuid);
    }

    @Test
    void shouldReturnTransactionNotFoundException() {
        UUID uuid = UUID.randomUUID();

        when(transactionRepository.findById(uuid)).thenReturn(Optional.empty());

        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransaction(uuid));

        verify(transactionRepository, times(1)).findById(uuid);
    }

    @Test
    void shouldGetTransactionsSuccess() {
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

        when(transactionRepository.findAll()).thenReturn(transactions);

        List<Transaction> result = transactionService.getTransactions();

        assertNotNull(result);
        assertEquals(transaction1.getSource(), result.get(0).getSource());
        assertEquals(transaction1.getAmount(), result.get(0).getAmount());
        assertEquals(transaction2.getSource(), result.get(1).getSource());
        assertEquals(transaction2.getAmount(), result.get(1).getAmount());

        verify(transactionRepository, times(1)).findAll();
    }

    @Test
    void shouldCreateTransactionSuccess() {
        Transaction transaction = new Transaction();
        transaction.setReference(UUID.randomUUID());
        transaction.setSource("123");
        transaction.setDestination("456");
        transaction.setAmount(BigDecimal.valueOf(1));
        transaction.setProtocol(ProtocolType.BTC);
        transaction.setDate(Instant.now());
        transaction.setFee(BigDecimal.valueOf(0.1));
        transaction.setDescription("Test transaction");

        Account sourceAccount = new Account();
        sourceAccount.setId("123");
        sourceAccount.setBalance(BigDecimal.valueOf(5));
        sourceAccount.setProtocol(ProtocolType.BTC);
        User sourceUser = new User();
        sourceUser.setId(1L);
        sourceAccount.setUser(sourceUser);

        Account destinationAccount = new Account();
        destinationAccount.setId("456");
        destinationAccount.setBalance(BigDecimal.valueOf(10));
        destinationAccount.setProtocol(ProtocolType.BTC);
        User destinationUser = new User();
        destinationUser.setId(2L);
        destinationAccount.setUser(destinationUser);

        when(accountService.getAccountByWalletAndProtocol(transaction.getSource(), transaction.getProtocol()))
                .thenReturn(sourceAccount);
        when(accountService.getAccountByWalletAndProtocol(transaction.getDestination(), transaction.getProtocol()))
                .thenReturn(destinationAccount);
        when(accountService.updateAccount(sourceAccount, sourceAccount.getBalance().subtract(transaction.getAmount()).subtract(transaction.getFee())))
                .thenReturn(sourceAccount);
        when(accountService.updateAccount(destinationAccount, destinationAccount.getBalance().add(transaction.getAmount())))
                .thenReturn(destinationAccount);
        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.createTransaction(transaction);

        assertNotNull(result);
        assertEquals("123", result.getSource());
        assertEquals("456", result.getDestination());
        assertEquals(BigDecimal.valueOf(1), result.getAmount());
        assertEquals(BigDecimal.valueOf(0.1), result.getFee());
        assertEquals(ProtocolType.BTC, result.getProtocol());
    }

    @Test
    void shouldReturnAccountOverdrawnException() {
        Transaction transaction = new Transaction();
        transaction.setReference(UUID.randomUUID());
        transaction.setSource("123");
        transaction.setDestination("456");
        transaction.setAmount(BigDecimal.valueOf(1));
        transaction.setProtocol(ProtocolType.BTC);
        transaction.setDate(Instant.now());
        transaction.setFee(BigDecimal.valueOf(0.1));
        transaction.setDescription("Test transaction");

        Account sourceAccount = new Account();
        sourceAccount.setId("123");
        sourceAccount.setBalance(BigDecimal.valueOf(-1));
        sourceAccount.setProtocol(ProtocolType.BTC);
        User sourceUser = new User();
        sourceUser.setId(1L);
        sourceAccount.setUser(sourceUser);

        when(accountService.getAccountByWalletAndProtocol(transaction.getSource(), transaction.getProtocol()))
                .thenReturn(sourceAccount);

        assertThrows(AccountOverdrawnException.class, () -> transactionService.createTransaction(transaction));
    }

    @Test
    void shouldReturnAccountBalanceNotEnoughException() {
        Transaction transaction = new Transaction();
        transaction.setReference(UUID.randomUUID());
        transaction.setSource("123");
        transaction.setDestination("456");
        transaction.setAmount(BigDecimal.valueOf(10));
        transaction.setProtocol(ProtocolType.BTC);
        transaction.setDate(Instant.now());
        transaction.setFee(BigDecimal.valueOf(0.1));
        transaction.setDescription("Test transaction");

        Account sourceAccount = new Account();
        sourceAccount.setId("123");
        sourceAccount.setBalance(BigDecimal.valueOf(9));
        sourceAccount.setProtocol(ProtocolType.BTC);
        User sourceUser = new User();
        sourceUser.setId(1L);
        sourceAccount.setUser(sourceUser);

        when(accountService.getAccountByWalletAndProtocol(transaction.getSource(), transaction.getProtocol()))
                .thenReturn(sourceAccount);

        assertThrows(AccountBalanceNotEnoughException.class, () -> transactionService.createTransaction(transaction));
    }

    @Test
    void shouldGetTransactionsMovementsSuccess() {
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

        when(transactionRepository.findBySourceOrDestination("account2","account2"))
                .thenReturn(transactions);

        List<Transaction> result = transactionService.getMovements("account2");

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(transaction1.getSource(), result.get(0).getSource());
        assertEquals(transaction1.getAmount(), result.get(0).getAmount());
        assertEquals(transaction2.getSource(), result.get(1).getSource());
        assertEquals(transaction2.getAmount(), result.get(1).getAmount());

        verify(transactionRepository, times(1))
                .findBySourceOrDestination("account2","account2");
    }
}