package com.exm.expensemanager.nbp.dto;

import java.math.BigDecimal;

public record ExchangeResponseDTO(
        String fromCurrency,
        String toCurrency,
        BigDecimal originalAmount,
        BigDecimal exchangedAmount,
        BigDecimal rate,
        BigDecimal srcNbpRate,
        BigDecimal dstNbpRate
) {
}
