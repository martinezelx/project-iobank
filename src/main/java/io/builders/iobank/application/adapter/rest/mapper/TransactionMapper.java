package io.builders.iobank.application.adapter.rest.mapper;

import io.builders.iobank.domain.model.Transaction;
import io.builders.iobank.application.adapter.rest.dto.TransactionDto;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

  TransactionDto toDto(Transaction transaction);

  List<TransactionDto> listToDto(List<Transaction> transactions);

  Transaction toDomain(TransactionDto transactionDto);
}
