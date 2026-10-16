package com.exm.expensemanager.common;

import lombok.*;

public record CurrencyInfo (
    String code,
    String name,
    String symbol,
    int decimal_digits
){};
