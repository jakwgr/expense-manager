package com.exm.expensemanager.user.dto;

import com.exm.expensemanager.common.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank(message = "Name is required")
        String name,

        @NotBlank(message = "Last name is required")
        String lastName,

        @NotNull(message = "Age is required")
        Integer age,

        @NotBlank(message = "Currency is required")
        String currency
) {
}
