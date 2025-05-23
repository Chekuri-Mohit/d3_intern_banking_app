package com.banking.mapper;
import com.banking.model.Payment;
import com.banking.schema.PaymentRequestDto;
import com.banking.schema.PaymentResponseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponseDto toDto(Payment payment);
    Payment toEntity(PaymentRequestDto dto);

}
