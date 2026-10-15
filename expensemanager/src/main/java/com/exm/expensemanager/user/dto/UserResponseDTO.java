package com.exm.expensemanager.user.dto;

import com.exm.expensemanager.common.enums.Currency;

public record UserResponseDTO(
        int id,
        String name,
        String lastName,
        int age,
        Currency currency
) {
}
