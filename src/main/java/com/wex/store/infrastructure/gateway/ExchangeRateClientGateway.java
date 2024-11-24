package com.wex.store.infrastructure.gateway;

import com.wex.store.application.gateway.ExchangeRateGateway;
import com.wex.store.domain.ExchangeRate;
import com.wex.store.infrastructure.gateway.integration.ExchangeRateClient;

import java.time.LocalDate;
import java.util.Optional;

public class ExchangeRateClientGateway implements ExchangeRateGateway {

    private final ExchangeRateClient client;

    public ExchangeRateClientGateway(ExchangeRateClient client) {
        this.client = client;
    }

    @Override
    public Optional<ExchangeRate> findExchangeRate(String country, LocalDate oldestAllowedDate) {
        return client.getExchange(country, oldestAllowedDate);
    }
}
