package com.banking.mapper;

import com.banking.dto.PaymentRequestDto;
import com.banking.dto.PaymentResponseDto;
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
            payment.getAmount(),
            payment.getNote(),
            payment.getPayeeType(),
            payment.getPaymentDate(),
            payment.getStatus(),
            payment.getFromAccount().getId(),
            payment.getToAccount().getId()
        );
    }
}

