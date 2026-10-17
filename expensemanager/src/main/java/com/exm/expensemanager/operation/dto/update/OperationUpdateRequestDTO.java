package com.exm.expensemanager.operation.dto.update;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperationUpdateRequestDTO (
        String title,

        @PastOrPresent(message = "Date must be in the past or present")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate operationDate,

        @PositiveOrZero(message = "Amount needs to be higher or equal zero")
        BigDecimal amount,

        String currency,

        String type
)
{};
