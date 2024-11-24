package com.wex.store.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public record Transaction(Long id, String description, LocalDate date, BigDecimal amount) {

}
