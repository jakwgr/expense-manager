package com.exm.expensemanager.nbp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record NbpRateResponse(
        String code,
        List<Rate> rates
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Rate(
            String effectiveDate,
            Double mid
    ) {}
}