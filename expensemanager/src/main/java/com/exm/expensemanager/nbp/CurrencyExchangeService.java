package com.exm.expensemanager.nbp;

import com.exm.expensemanager.common.Util;
import com.exm.expensemanager.nbp.dto.ExchangeResponseDTO;
import com.exm.expensemanager.nbp.dto.SummaryResultDTO;
import com.exm.expensemanager.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private final NbpUtil nbpUtil;
    private final Util util;
    private final UserService userService;

    public ExchangeResponseDTO exchangeCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        BigDecimal safeAmount = (amount == null) ? BigDecimal.ZERO : amount;

        String fromUpper = (fromCurrency == null || fromCurrency.isBlank()) ? userService.getUserCurrencyInformation().code() : fromCurrency.trim().toUpperCase();
        String toUpper = (toCurrency == null || toCurrency.isBlank()) ? userService.getUserCurrencyInformation().code() : toCurrency.trim().toUpperCase();

        int targetDecimals = util.getCurrencyInformation(toUpper).decimal_digits();

        if (fromUpper.equals(toUpper) || safeAmount.compareTo(BigDecimal.ZERO) == 0) {
            return new ExchangeResponseDTO(
                    fromUpper,
                    toUpper,
                    safeAmount,
                    safeAmount.setScale(targetDecimals, RoundingMode.HALF_UP),
                    BigDecimal.ONE,
                    BigDecimal.ONE,
                    BigDecimal.ONE
            );
        }

        Map<String, BigDecimal> srcRateMap = nbpUtil.getAverageExchangeRate(fromUpper);
        Map<String, BigDecimal> dstRateMap = nbpUtil.getAverageExchangeRate(toUpper);

        BigDecimal srcRate = (srcRateMap != null && srcRateMap.containsKey(fromUpper))
                ? srcRateMap.get(fromUpper)
                : BigDecimal.ONE;

        BigDecimal dstRate = (dstRateMap != null && dstRateMap.containsKey(toUpper))
                ? dstRateMap.get(toUpper)
                : BigDecimal.ONE;

        BigDecimal effectiveRate = srcRate.divide(dstRate, 4, RoundingMode.HALF_UP);

        BigDecimal exchangedAmount = safeAmount.multiply(effectiveRate).setScale(
                targetDecimals,
                RoundingMode.HALF_UP
        );

        return new ExchangeResponseDTO(
                fromUpper,
                toUpper,
                safeAmount,
                exchangedAmount,
                effectiveRate,
                srcRate,
                dstRate
        );
    }

    public SummaryResultDTO calculateTotalSummary(Map<String, BigDecimal> operationSummary, String baseCurrency) {
        String targetCurrency = (baseCurrency == null || baseCurrency.isBlank()) ? userService.getUserCurrencyInformation().code() : baseCurrency.trim().toUpperCase();

        if (operationSummary == null || operationSummary.isEmpty()) {
            return new SummaryResultDTO(targetCurrency, Map.of(), Map.of(), Map.of(), BigDecimal.ZERO);
        }

        Map<String, BigDecimal> convertedAmounts = new HashMap<>();
        Map<String, BigDecimal> rates = new HashMap<>();
        BigDecimal totalSum = BigDecimal.ZERO;

        for (Map.Entry<String, BigDecimal> entry : operationSummary.entrySet()) {
            String currency = entry.getKey() == null ? userService.getUserCurrencyInformation().code() : entry.getKey().trim().toUpperCase();
            BigDecimal amount = entry.getValue() == null ? BigDecimal.ZERO : entry.getValue();

            if (amount.compareTo(BigDecimal.ZERO) == 0) {
                convertedAmounts.put(currency, BigDecimal.ZERO);
                rates.put(currency, BigDecimal.ONE);
                continue;
            }

            BigDecimal absAmount = amount.abs();
            ExchangeResponseDTO exchangeResult = exchangeCurrency(absAmount, currency, targetCurrency);

            BigDecimal appliedRate = exchangeResult.rate();
            rates.put(currency, appliedRate);

            BigDecimal convertedAmount = amount.signum() < 0
                    ? exchangeResult.exchangedAmount().negate()
                    : exchangeResult.exchangedAmount();

            convertedAmounts.put(currency, convertedAmount);
            totalSum = totalSum.add(convertedAmount);
        }

        int baseDecimals = util.getCurrencyInformation(targetCurrency).decimal_digits();
        totalSum = totalSum.setScale(baseDecimals, RoundingMode.HALF_UP);

        return new SummaryResultDTO(
                targetCurrency,
                operationSummary,
                convertedAmounts,
                rates,
                totalSum
        );
    }
}