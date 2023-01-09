package io.builders.iobank.domain.service;

import io.builders.iobank.domain.model.Transaction;

import java.util.List;
import java.util.UUID;

public interface TransactionService {
    Transaction getTransaction(UUID id);

    List<Transaction> getMovements(String accountId);

    List<Transaction> getTransactions();

    Transaction createTransaction(Transaction transaction);
}
