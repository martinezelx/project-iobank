package io.builders.iobank.domain.service.impl;

import io.builders.iobank.domain.exceptions.AccountNotFoundException;
import io.builders.iobank.domain.model.Account;
import io.builders.iobank.domain.model.ProtocolType;
import io.builders.iobank.domain.port.repository.database.AccountRepository;
import io.builders.iobank.domain.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Service
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public Account getAccount(String id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException("Account with id " + id + " not found"));
    }

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountByWalletAndProtocol(String id, ProtocolType protocol) {
        return accountRepository.findByIdAndProtocol(id, protocol)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account with id " + id + " and protocol " + protocol + " not found"));
    }

    @Override
    public Account updateAccount(Account accountToUpdate, BigDecimal balance) {
        accountToUpdate.setBalance(balance);
        return accountRepository.save(accountToUpdate);
    }

    @Override
    public Account depositBalanceInAccount(String id, BigDecimal newBalance) {
        Account account = getAccount(id);
        account.setBalance(account.getBalance().add(newBalance));
        return accountRepository.save(account);
    }
}
