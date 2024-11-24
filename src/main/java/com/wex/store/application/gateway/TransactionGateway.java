package com.wex.store.application.gateway;

import com.wex.store.domain.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionGateway {

    Long save(Transaction transaction);

    Optional<Transaction> findOne(Long id);

    List<Transaction> findAll();
}
