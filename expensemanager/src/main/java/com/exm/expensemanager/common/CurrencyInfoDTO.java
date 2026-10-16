package com.exm.expensemanager.common;

public record CurrencyInfoDTO(
    String code,
    String name,
    String symbol,
    int decimal_digits
){};
