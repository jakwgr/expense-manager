package com.exm.expensemanager.nbp;

import com.exm.expensemanager.common.Util;
import com.exm.expensemanager.handler.exceptions.ExchangeOperationSumException;
import com.exm.expensemanager.handler.exceptions.NbpCurrencyException;
import com.exm.expensemanager.nbp.dto.ExchangeResponseDTO;
import lombok.RequiredArgsConstructor;
import org.apache.poi.hpsf.Decimal;
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

    public ExchangeResponseDTO exchangeCurrency(BigDecimal amount, String fromCurrency, String toCurrency) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) throw new ExchangeOperationSumException();

        String fromUpper = fromCurrency.trim().toUpperCase();
        String toUpper = toCurrency.trim().toUpperCase();

        if(fromUpper.equals(toUpper))
        {
            return new ExchangeResponseDTO(
                    fromUpper,
                    toUpper,
                    amount,
                    amount.setScale(
                            util.getCurrencyInformation(toUpper).decimal_digits()
                            , RoundingMode.HALF_UP),
                    BigDecimal.ONE,
                    BigDecimal.ONE,
                    BigDecimal.ONE
            );
        }

        Map<String, BigDecimal> srcRateMap = nbpUtil.getAverageExchangeRate(fromUpper);
        Map<String, BigDecimal> dstRateMap = nbpUtil.getAverageExchangeRate(toUpper);

        BigDecimal srcRate = srcRateMap.get(fromUpper);
        BigDecimal dstRate = dstRateMap.get(toUpper);

        BigDecimal effectiveRate = srcRate.divide(dstRate, 4, RoundingMode.HALF_UP);

        BigDecimal exchangedAmount = amount.multiply(effectiveRate).setScale(
                util.getCurrencyInformation(toUpper).decimal_digits()
                , RoundingMode.HALF_UP);

        return new ExchangeResponseDTO(
                fromUpper,
                toUpper,
                amount,
                exchangedAmount,
                effectiveRate,
                srcRate,
                dstRate
        );
    }
}
