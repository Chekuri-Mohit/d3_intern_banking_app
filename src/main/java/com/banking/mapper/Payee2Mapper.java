package com.banking.mapper;

import com.banking.schema.PayeeRequestDto;
import com.banking.schema.PayeeResponseDto;
import com.banking.model.Payee;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface Payee2Mapper {
    Payee toEntity(PayeeRequestDto payeeRequestDto);

    PayeeResponseDto toDto(Payee payee);

    List<PayeeResponseDto> toPayeeResponseDtoList(List<Payee> payees);
}
