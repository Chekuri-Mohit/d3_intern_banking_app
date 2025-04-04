package com.banking.mapper;

import com.banking.schema.PaymentDetailDto;
import com.banking.schema.PaymentRequestDto;
import com.banking.schema.PaymentResponseDto;
import com.banking.model.Payment;
import org.mapstruct.Mapper;

@Mapper
public class PaymentMapper {

    // Convert DTO to Entity
    public static Payment toEntity(PaymentRequestDto dto) {
        Payment payment = new Payment();
        payment.setAmount(dto.getAmount());
        payment.setNote(dto.getNote());
        payment.setPayeeType(dto.getPayeeType());
        payment.setPaymentDate(dto.getPaymentDate());
        payment.setStatus(dto.getStatus());
        return payment;
    }

    // Convert Entity to DTO
    public static PaymentResponseDto toDto(Payment payment) {
        return new PaymentResponseDto(
            payment.getId(),
            payment.getFromAccount().getId(),
            payment.getToAccount().getId(),
            payment.getPaymentDate(),
            payment.getAmount(),
            payment.getStatus(),
            payment.getPayeeType(),
            payment.getNote()
        );
    }

    public static PaymentDetailDto toDetailDto(Payment payment) {
        return new PaymentDetailDto(
            payment.getId(),
            payment.getFromAccount().getAccountNumber(),
            payment.getToAccount().getAccountNumber(),
            payment.getPaymentDate(),
            payment.getAmount(),
            payment.getStatus(),
            payment.getPayeeType(),
            payment.getNote()
        );
    }

}

