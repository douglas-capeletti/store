package com.wex.store.application.gateway;

import com.wex.store.domain.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TransactionGatewayStub implements TransactionGateway {

    private final List<Transaction> data = new ArrayList<>();

    @Override
    public Long save(Transaction transaction) {
        data.add(transaction);
        return transaction.id();
    }

    @Override
    public Optional<Transaction> findOne(Long id) {
        return data.stream().filter(transaction -> transaction.id().equals(id)).findFirst();
    }

    @Override
    public List<Transaction> findAll() {
        return data;
    }
}