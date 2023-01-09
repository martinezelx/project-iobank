package io.builders.iobank.application.adapter.rest.mapper;

import io.builders.iobank.domain.model.Account;
import io.builders.iobank.application.adapter.rest.dto.AccountDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(source = "user.id", target = "userId")
    AccountDto toDto(Account account);

    List<AccountDto> listToDto(List<Account> accounts);

    @Mapping(source = "userId", target = "user.id")
    Account toDomain(AccountDto accountDto);
}
