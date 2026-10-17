package com.exm.expensemanager.operation.dto.create;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record OperationRequestUserDTO(
        @NotBlank(message = "Field \"title\" is required")
        String title,

        @NotNull(message = "Field \"date\" is required")
        @PastOrPresent(message = "Date must be in the past or present")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate operationDate,

        @NotNull(message = "Field \"amount\" is required")
        @PositiveOrZero(message = "Amount needs to be higher or equal zero")
        BigDecimal amount,

        @NotBlank(message = "Field \"currency\" is required")
        String currency,

        @NotBlank(message = "Field \"type\" is required")
        String type,

        @NotNull(message = "Field \"included\" is required")
        String included
) {
}
