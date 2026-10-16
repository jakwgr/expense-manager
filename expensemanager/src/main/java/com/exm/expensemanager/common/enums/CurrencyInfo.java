package com.exm.expensemanager.common.enums;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class CurrencyInfo {
    String code;
    String name;
    String symbol;
    int decimal_digits;
}
