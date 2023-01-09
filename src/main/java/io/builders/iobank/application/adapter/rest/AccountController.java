package io.builders.iobank.application.adapter.rest;

import io.builders.iobank.application.adapter.rest.dto.AccountDto;
import io.builders.iobank.application.adapter.rest.mapper.AccountMapper;
import io.builders.iobank.domain.service.AccountService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/iobank/accounts")
@Validated
public class AccountController {

    private final AccountService accountService;

    private final AccountMapper accountMapper;

    @PostMapping("/create")
    public ResponseEntity<AccountDto> createAccount(@Valid @RequestBody AccountDto accountDto) {
        return new ResponseEntity<>(accountMapper.toDto(accountService.saveAccount(
                accountMapper.toDomain(accountDto))),
                HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<AccountDto>> getAccounts() {
        return new ResponseEntity<>(
                accountMapper.listToDto(accountService.getAccounts()),
                HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<AccountDto> getAccountById(@PathVariable String id) {
        return new ResponseEntity<>(
                accountMapper.toDto(accountService.getAccount(id)),
                HttpStatus.OK);
    }

    @PutMapping("/deposit/{id}")
    public ResponseEntity<AccountDto> updateBalance(@PathVariable String id,
                                                    @RequestParam @Positive BigDecimal balance) {
        return new ResponseEntity<>(
                accountMapper.toDto(accountService.depositBalanceInAccount(id, balance)),
                HttpStatus.OK);
    }
}
