package com.exm.expensemanager.nbp;

import com.exm.expensemanager.nbp.dto.NbpRateResponse;
import com.exm.expensemanager.handler.exceptions.NbpApiConnectionException;
import com.exm.expensemanager.handler.exceptions.NbpCurrencyException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Component
public class NbpUtil {

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, BigDecimal> getAverageExchangeRate(String code)
    {
//        if(code == null || code.isBlank()) throw new NbpCurrencyException();
        if(code == null || code.isBlank()) {
                return Map.of("UNKNOWN", BigDecimal.ONE);
        }

        Map<String, BigDecimal> exchangeRate = new HashMap<>();
        code = code.toLowerCase();

        if(Objects.equals(code, "pln")) {
            exchangeRate.put("PLN",BigDecimal.ONE);
            return exchangeRate;
        }

        String url = String.format("https://api.nbp.pl/api/exchangerates/rates/a/%s/?format=json", code.trim());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        try{
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200)
            {
                NbpRateResponse rateData = objectMapper.readValue(response.body(), NbpRateResponse.class);

                Double mid = rateData.rates().getFirst().mid();
                exchangeRate.put(code.toUpperCase(), BigDecimal.valueOf(mid));

                return exchangeRate;
            }
        } catch (IOException | InterruptedException e) {
        }
        exchangeRate.put(code.toUpperCase(), BigDecimal.ONE);
        return exchangeRate;
    }
}
