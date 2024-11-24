package com.wex.store.application.gateway;

import com.wex.store.domain.ExchangeRate;

import java.time.LocalDate;
import java.util.Optional;

public class ExchangeRateGatewayStub implements ExchangeRateGateway {

    @Override
    public Optional<ExchangeRate> findExchangeRate(String country, LocalDate oldestAllowedDate) {
        if ("Brazil".equalsIgnoreCase(country) && LocalDate.of(2024, 6, 12).isAfter(oldestAllowedDate)){
           return Optional.of(new ExchangeRate("Brazil", LocalDate.of(2024, 10, 31), 3.75D, "Real"));
        }
        return Optional.empty();
    }
}