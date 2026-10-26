package com.exm.expensemanager.nbp.dto;

import java.math.BigDecimal;
import java.util.Map;

public record SummaryResultDTO(
        String baseCurrency,
        Map<String, BigDecimal> originalAmounts,
        Map<String, BigDecimal> convertedAmounts,
        Map<String, BigDecimal> rates,
        BigDecimal totalSum
) {
}
