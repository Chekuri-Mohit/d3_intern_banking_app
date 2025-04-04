package com.banking.mapper;
//

import com.banking.model.Account;
import com.banking.schema.AccountRequestDto;
import com.banking.schema.AccountResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    @Mapping(target = "accountNumber", expression = "java(generateAccountNumber())")
    @Mapping(target = "balance", constant = "0")
    Account toEntity(AccountRequestDto accountRequestDto);

    AccountResponseDto toAccountResponseDto(Account account);

    List<AccountResponseDto> toAccountResponseDtoList(List<Account> accounts);

    default String generateAccountNumber() {
        return java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}