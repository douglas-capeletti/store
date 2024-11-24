package com.wex.store.infrastructure.controller;

import com.wex.store.application.usecase.TransactionInteractor;
import com.wex.store.domain.Transaction;
import com.wex.store.domain.TransactionExchangeRate;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionInteractor transactionInteractor;

    public TransactionController(TransactionInteractor transactionInteractor) {
        this.transactionInteractor = transactionInteractor;
    }

    @PostMapping("/purchase")
    public Long newPurchase(@RequestBody NewPurchaseRequestModel request) {
        Transaction transaction = new Transaction(null, request.getDescription(), request.getTransactionDate(), request.getAmount());
        return transactionInteractor.newPurchase(transaction);
    }

    @GetMapping("/purchase")
    public List<Transaction> findAllPurchases() {
        return transactionInteractor.findAllPurchases();
    }

    @GetMapping("/purchase/{id}")
    public Transaction findPurchase(@PathVariable Long id) {
        return transactionInteractor.findPurchase(id);
    }

    @GetMapping("/purchase/{id}/exchange-rate/country/{country}")
    public TransactionExchangeRateResponseModel findPurchase(@PathVariable Long id, @PathVariable String country) {
        TransactionExchangeRate domainResponse = transactionInteractor.findPurchaseWithCurrency(id, country);
        return new TransactionExchangeRateResponseModel(domainResponse.originalTransaction(), domainResponse.usedRate(), domainResponse.resultAmount());
    }


}
