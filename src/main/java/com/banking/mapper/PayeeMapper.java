package com.banking.mapper;

import com.banking.schema.PayeeRequestDto;
import com.banking.schema.PayeeResponseDto;
import com.banking.model.Payee;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
@Component
public class PayeeMapper {
    public Payee toEntity(PayeeRequestDto dto) {
        Payee payee = new Payee();
        payee.setName(dto.getName());
        payee.setNickname(dto.getNickname());
        payee.setAddressLine1(dto.getAddressLine1());
        payee.setAddressLine2(dto.getAddressLine2());
        payee.setCity(dto.getCity());
        payee.setState(dto.getState());
        payee.setZip(dto.getZip());
        payee.setPhoneNumber((dto.getPhoneNumber()));
        payee.setAccountNumber(dto.getAccountNumber());
        payee.setStatus(dto.getStatus());
        return payee;
    }

    public PayeeResponseDto toDto(Payee payee) {
        return new PayeeResponseDto(
            payee.getId(),
            payee.getName(),
            payee.getNickname(),
            payee.getAddressLine1(),
            payee.getAddressLine2(),
            payee.getCity(),
            payee.getZip(),
            payee.getStatus(),
            payee.getPhoneNumber(),
            payee.getAccountNumber(),
            payee.getStatus()

        );
    }
}
