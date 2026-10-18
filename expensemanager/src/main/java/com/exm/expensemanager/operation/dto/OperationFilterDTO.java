package com.exm.expensemanager.operation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperationFilterDTO(
        Integer id,

        String title,

        @PastOrPresent(message = "Date must be in the past or present")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate operationDateFrom,

        @PastOrPresent(message = "Date must be in the past or present")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate operationDateTo,

        @PositiveOrZero(message = "Amount needs to be higher or equal zero")
        BigDecimal amountMin,

        @PositiveOrZero(message = "Amount needs to be higher or equal zero")
        BigDecimal amountMax,

        String currency,

        String type,

        Boolean included,

        String operationType
) {
}
