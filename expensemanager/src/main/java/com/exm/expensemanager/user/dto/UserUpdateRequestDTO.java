package com.exm.expensemanager.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateRequestDTO(
        String name,
        String lastName,
        Integer age,
        String currency
) {
}
