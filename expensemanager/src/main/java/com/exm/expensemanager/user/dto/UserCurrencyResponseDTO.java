package com.exm.expensemanager.user.dto;

import com.exm.expensemanager.common.enums.Currency;

public record UserCurrencyResponseDTO(
        Currency currency
) {
}
