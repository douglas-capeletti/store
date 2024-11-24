package com.wex.store.application.gateway;

import com.wex.store.domain.ExchangeRate;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeRateGateway {

    Optional<ExchangeRate> findExchangeRate(String country, LocalDate oldestAllowedDate);
}
