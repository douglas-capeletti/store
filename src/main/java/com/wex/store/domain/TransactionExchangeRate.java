package com.wex.store.domain;

import java.math.BigDecimal;

public record TransactionExchangeRate(Transaction originalTransaction, ExchangeRate usedRate, BigDecimal resultAmount) {
}
