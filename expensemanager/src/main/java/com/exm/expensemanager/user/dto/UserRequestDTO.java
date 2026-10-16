package com.exm.expensemanager.user.dto;

import com.exm.expensemanager.common.enums.Currency;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRequestDTO(
        @NotBlank(message = "Field \"name\" is required")
        String name,

        @NotBlank(message = "Field \"last name\" is required")
        String lastName,

        @NotNull(message = "Field \"age\" is required")
        Integer age,

        @NotBlank(message = "Field \"currency\" is required")
        String currency
) {
}
