package com.exm.expensemanager.user.dto;

import com.exm.expensemanager.common.enums.Currency;

public record UserRequestEnumDTO(
        String name,
        String lastName,
        Integer age,
        Currency currency
) {
}
