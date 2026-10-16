package com.exm.expensemanager.operation.dto;

import com.exm.expensemanager.common.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperationResponseDTO(
        Integer id,
        String title,
        LocalDate operationDate,
        BigDecimal amount,
        String currency,
        String type,
        Boolean included,
        String operationType
) {
}
