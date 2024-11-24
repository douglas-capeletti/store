package com.wex.store.infrastructure.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wex.store.domain.ExchangeRate;
import com.wex.store.domain.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionExchangeRateResponseModel {

    @JsonProperty("original_transaction")
    private Transaction originalTransaction;
    @JsonProperty("used_rate")
    private ExchangeRate usedRate;
    @JsonProperty("result_amount")
    private BigDecimal resultAmount;

    public TransactionExchangeRateResponseModel() {
    }

    public TransactionExchangeRateResponseModel(Transaction originalTransaction, ExchangeRate usedRate, BigDecimal resultAmount) {
        this.originalTransaction = originalTransaction;
        this.usedRate = usedRate;
        this.resultAmount = resultAmount.setScale(2, RoundingMode.DOWN);
    }

    public Transaction getOriginalTransaction() {
        return originalTransaction;
    }

    public void setOriginalTransaction(Transaction originalTransaction) {
        this.originalTransaction = originalTransaction;
    }

    public ExchangeRate getUsedRate() {
        return usedRate;
    }

    public void setUsedRate(ExchangeRate usedRate) {
        this.usedRate = usedRate;
    }

    public BigDecimal getResultAmount() {
        return resultAmount;
    }

    public void setResultAmount(BigDecimal resultAmount) {
        this.resultAmount = resultAmount;
    }
}
