package com.exm.expensemanager.user.dto;

import com.exm.expensemanager.common.enums.Currency;

public record UserRequestDTO(
        String name,
        String last,
        int age,
        Currency currency
) {
}
