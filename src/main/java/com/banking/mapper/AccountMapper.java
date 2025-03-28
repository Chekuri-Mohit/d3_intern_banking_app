package com.banking.mapper;
//

import com.banking.dto.AccountRequestDto;
import com.banking.dto.AccountResponseDto;
import com.banking.model.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mapping(target = "accountNumber", expression = "java(generateAccountNumber())")
    @Mapping(target = "balance", constant = "0")
    Account toEntity(AccountRequestDto accountRequestDto);

    AccountResponseDto toAccountResponseDto(Account account);

    List<AccountResponseDto> toAccountResponseDtoList(List<Account> accounts);

    default String generateAccountNumber() {
        return java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
}