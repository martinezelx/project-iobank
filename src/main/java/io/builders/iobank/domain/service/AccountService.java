package io.builders.iobank.domain.service;

import io.builders.iobank.domain.model.Account;
import io.builders.iobank.domain.model.ProtocolType;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    Account getAccount(String id);

    List<Account> getAccounts();

    Account saveAccount(Account account);

    Account getAccountByWalletAndProtocol(String id, ProtocolType protocol);

    Account updateAccount(Account account, BigDecimal balance);

    Account depositBalanceInAccount(String id, BigDecimal balance);
}
