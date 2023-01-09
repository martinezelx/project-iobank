package io.builders.iobank.application.adapter.rest;

import io.builders.iobank.application.adapter.rest.dto.TransactionDto;
import io.builders.iobank.application.adapter.rest.mapper.TransactionMapper;
import io.builders.iobank.domain.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    private final TransactionMapper transactionMapper;

    @PostMapping("/create")
    public ResponseEntity<TransactionDto> createTransaction(@Valid @RequestBody TransactionDto transactionDto) {
        return new ResponseEntity<>(transactionMapper.toDto(transactionService.createTransaction(
                transactionMapper.toDomain(transactionDto))),
                HttpStatus.CREATED);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TransactionDto>> getTransactions() {
        return new ResponseEntity<>(
                transactionMapper.listToDto(transactionService.getTransactions()),
                HttpStatus.OK);
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<TransactionDto> getTransactionById(@PathVariable UUID id) {
        return new ResponseEntity<>(
                transactionMapper.toDto(transactionService.getTransaction(id)),
                HttpStatus.OK);
    }

    @GetMapping("/movements/{accountId}")
    public ResponseEntity<List<TransactionDto>> getTransactionForAccount(@PathVariable String accountId) {
        return new ResponseEntity<>(
                transactionMapper.listToDto(transactionService.getMovements(accountId)),
                HttpStatus.OK);
    }
}
