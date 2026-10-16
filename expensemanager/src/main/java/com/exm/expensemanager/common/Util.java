package com.exm.expensemanager.common;

import com.exm.expensemanager.common.enums.Currency;
import com.exm.expensemanager.handler.exceptions.UserNotExists;
import com.exm.expensemanager.handler.exceptions.WrongCurrencyException;
import com.exm.expensemanager.user.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class Util {

    private final UserRepository userRepository;
    private final Map<String, CurrencyInfo> currencyInfoCache = new HashMap<>();

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
                    CurrencyInfo currencyInfo = new CurrencyInfo(
                            code,
                            item.path("name").asText(),
                            item.path("symbol").asText(),
                            item.path("decimal_digits").asInt()
                    );
                    currencyInfoCache.put(code.toUpperCase(), currencyInfo);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Cannot load currency.json file");
        }
    }

    public int getCurrentUserId()
    {
        var activeUser = this.userRepository.findTopByOrderByIdDesc().orElseThrow(UserNotExists::new);

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

    public CurrencyInfo getCurrencyInformation(String value)
    {
        if(value == null || value.isBlank()) throw new RuntimeException("Currency value cannot be empty!");
        if(!doesCurrencyContains(value)) throw new WrongCurrencyException();

        System.out.println(value);
        System.out.println(currencyInfoCache.get(value.toUpperCase()));
        return currencyInfoCache.get(value.toUpperCase());
    }

    public CurrencyInfo getUserCurrencyInfo()
    {
        int userId = this.getCurrentUserId();
        String value = String.valueOf(this.userRepository.findById(userId).orElseThrow(UserNotExists::new).getCurrency());

        return this.getCurrencyInformation(value);
    }
}
