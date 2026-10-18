package com.exm.expensemanager.operation.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record OperationReportDTO(
        @NotNull(message = "Field \"date\" is required")
        @PastOrPresent(message = "Date must be in the past or present")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate start,

        @NotNull(message = "Field \"date\" is required")
        @PastOrPresent(message = "Date must be in the past or present")
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate end
) {
}
