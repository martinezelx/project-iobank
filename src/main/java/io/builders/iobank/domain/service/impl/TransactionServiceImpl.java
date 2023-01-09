package io.builders.iobank.domain.service.impl;

import io.builders.iobank.domain.exceptions.AccountBalanceNotEnoughException;
import io.builders.iobank.domain.exceptions.AccountOverdrawnException;
import io.builders.iobank.domain.exceptions.TransactionNotFoundException;
import io.builders.iobank.domain.model.Account;
import io.builders.iobank.domain.model.Transaction;
import io.builders.iobank.domain.port.repository.database.TransactionRepository;
import io.builders.iobank.domain.service.AccountService;
import io.builders.iobank.domain.service.TransactionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;

    private final AccountService accountService;

    @Override
    public Transaction getTransaction(UUID id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction with id " + id  + " not found"));
    }

    @Override
    public List<Transaction> getMovements(String accountId) {
        return transactionRepository.findBySourceOrDestination(accountId, accountId);
    }

    @Override
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        log.info("Start transaction from: " + transaction.getSource() + " to: " + transaction.getDestination()
                + " with protocol: " + transaction.getProtocol());

        final var sourceAccount =
                accountService.getAccountByWalletAndProtocol(transaction.getSource(), transaction.getProtocol());

        final var destinationAccount =
                accountService.getAccountByWalletAndProtocol(transaction.getDestination(), transaction.getProtocol());

        checkAccountIsNotOverdrawn(transaction, sourceAccount);

        checkAccountHasEnoughBalance(transaction, sourceAccount);

        final var newSourceBalance =
                sourceAccount.getBalance().subtract(transaction.getAmount()).subtract(transaction.getFee());
        accountService.updateAccount(sourceAccount,newSourceBalance);

        final var newDestinationBalance =
                destinationAccount.getBalance().add(transaction.getAmount());
        accountService.updateAccount(destinationAccount,newDestinationBalance);

        return transactionRepository.save(transaction);
    }

    private void checkAccountIsNotOverdrawn(Transaction transaction, Account sourceAccount) {
        if (sourceAccount.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new AccountOverdrawnException("Source account: "+ transaction.getSource() + " is overdrawn");
        }
    }

    private void checkAccountHasEnoughBalance(Transaction transaction, Account sourceAccount) {
        if (transaction.getAmount().add(transaction.getFee()).compareTo(sourceAccount.getBalance()) > 0) {
            throw new AccountBalanceNotEnoughException("Transaction amount is bigger than account balance");
        }
    }
}
