package com.wex.store.application.usecase;

import com.wex.store.application.exception.BusinessException;
import com.wex.store.application.exception.NotFoundException;
import com.wex.store.application.gateway.ExchangeRateGateway;
import com.wex.store.application.gateway.TransactionGateway;
import com.wex.store.domain.ExchangeRate;
import com.wex.store.domain.Transaction;
import com.wex.store.domain.TransactionExchangeRate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class TransactionInteractor {

    private final TransactionGateway transactionGateway;
    private final ExchangeRateGateway exchangeRateGateway;

    public TransactionInteractor(TransactionGateway transactionGateway, ExchangeRateGateway exchangeRateGateway) {
        this.transactionGateway = transactionGateway;
        this.exchangeRateGateway = exchangeRateGateway;
    }

    public Long newPurchase(Transaction newPurchaseTransaction) {
        List<String> errors = new ArrayList<>();
        if (newPurchaseTransaction.description() == null || newPurchaseTransaction.description().length() > 50) {
            errors.add("Invalid description. Description required, and must have a max size of 50 characters.");
        }
        if (newPurchaseTransaction.date() == null) {
            errors.add("Invalid transaction date. Transaction date must be a valid value.");
        }
        if (newPurchaseTransaction.amount() == null || newPurchaseTransaction.amount().signum() < 1) {
            errors.add("Invalid transaction amount. Transaction amount must be a valid and positive value.");
        }
        if (!errors.isEmpty()) {
            throw new BusinessException(errors);
        }
        return transactionGateway.save(newPurchaseTransaction);
    }

    public Transaction findPurchase(Long id) {
        return transactionGateway
                .findOne(id)
                .orElseThrow(() -> new NotFoundException("Transaction not found for id: " + id));
    }

    public List<Transaction> findAllPurchases() {
        return transactionGateway.findAll();
    }

    public TransactionExchangeRate findPurchaseWithCurrency(Long id, String country) {
        Transaction transaction = this.findPurchase(id);
        ExchangeRate exchangeRate = exchangeRateGateway
                .findExchangeRate(country, transaction.date().minusMonths(6))
                .orElseThrow(() -> new BusinessException("This transaction could not be converted to the target exchange rate"));
        BigDecimal resultRate = transaction.amount().multiply(BigDecimal.valueOf(exchangeRate.rate()));
        return new TransactionExchangeRate(
                transaction,
                exchangeRate,
                resultRate
        );
    }
}
