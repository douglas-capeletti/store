package com.wex.store.domain;

import java.time.LocalDate;

public record ExchangeRate(String country, LocalDate date, Double rate, String currency) {
}
