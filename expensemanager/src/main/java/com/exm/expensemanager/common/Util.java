package com.exm.expensemanager.common;

import com.exm.expensemanager.common.enums.Currency;
import com.exm.expensemanager.common.enums.OperationType;
import com.exm.expensemanager.handler.exceptions.user.UserNotExistsException;
import com.exm.expensemanager.handler.exceptions.WrongCurrencyException;
import com.exm.expensemanager.operation.Operation;
import com.exm.expensemanager.operation.dto.OperationResponseDTO;
import com.exm.expensemanager.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class Util {

    private final UserRepository userRepository;
    private final Map<String, CurrencyInfoDTO> currencyInfoCache = new HashMap<>();
    private final Object[] boolVariables = new Object[4];

    @PostConstruct
    public void initCurrencyData()
    {
        ObjectMapper mapper = new ObjectMapper();

        try {
            ClassPathResource resource = new ClassPathResource("static/currency.json");

            JsonNode rootArray = mapper.readTree(resource.getInputStream());
            rootArray = rootArray.get("currencies");
            for(JsonNode item : rootArray)
            {

                String code = item.path("code").asText();
                if(!code.isBlank())
                {
                    CurrencyInfoDTO currencyInfoDTO = new CurrencyInfoDTO(
                            code,
                            item.path("name").asText(),
                            item.path("symbol").asText(),
                            item.path("decimal_digits").asInt()
                    );
                    currencyInfoCache.put(code.toUpperCase(), currencyInfoDTO);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load currency.json file");
        }
    }

    @PostConstruct
    public void initBoolVariables()
    {
        try {
            boolVariables[0] = "TRUE";
            boolVariables[1] = "FALSE";
        } catch (RuntimeException e) {
            throw new RuntimeException("Cannot load bool variables array");
        }
    }

    public int getCurrentUserId()
    {
        var activeUser = this.userRepository.findTopByOrderByIdDesc().orElseThrow(UserNotExistsException::new);

        return activeUser.getId();
    }

    public boolean doesCurrencyContains(String value)
    {
        for (Currency currency : Currency.values())
        {
            if(currency.name().equals(value.toUpperCase()))
            {
                return true;
            }
        }
        return false;
    }

    public CurrencyInfoDTO getCurrencyInformation(String value)
    {
        if(value == null || value.isBlank()) throw new RuntimeException("Currency value cannot be empty!");
        if(!doesCurrencyContains(value)) throw new WrongCurrencyException();
        return currencyInfoCache.get(value.toUpperCase());
    }

    public CurrencyInfoDTO getUserCurrencyInfo()
    {
        int userId = this.getCurrentUserId();
        String value = String.valueOf(this.userRepository.findById(userId).orElseThrow(UserNotExistsException::new).getCurrency());

        return this.getCurrencyInformation(value);
    }

    public boolean isBoolCorrect(String value)
    {
        return Arrays.asList(boolVariables).contains(value.toUpperCase());
    }

    public BigDecimal convertExpenseOrIncome(OperationResponseDTO operation)
    {
        OperationType operationType = OperationType.valueOf(operation.operationType());
        CurrencyInfoDTO currencyInfo = getCurrencyInformation(operation.currency());

        BigDecimal amount = operation.amount().setScale(currencyInfo.decimal_digits(), RoundingMode.HALF_UP);

        if(operationType.equals(OperationType.EXPENSE))
        {
            return amount.abs().negate();
        }
        return amount.abs();
    }
}
