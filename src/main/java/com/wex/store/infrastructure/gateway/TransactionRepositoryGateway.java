package com.wex.store.infrastructure.gateway;

import com.wex.store.application.gateway.TransactionGateway;
import com.wex.store.domain.Transaction;
import com.wex.store.infrastructure.gateway.persistense.TransactionDatasource;
import com.wex.store.infrastructure.gateway.persistense.TransactionEntity;
import com.wex.store.infrastructure.gateway.persistense.TransactionEntityMapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TransactionRepositoryGateway implements TransactionGateway {

    private final TransactionDatasource jpa;
    private final TransactionEntityMapper entityMapper;

    public TransactionRepositoryGateway(TransactionDatasource jpa, TransactionEntityMapper entityMapper) {
        this.jpa = jpa;
        this.entityMapper = entityMapper;
    }

    @Override
    public Long save(Transaction transaction) {
        TransactionEntity entity = entityMapper.toEntity(transaction);
        TransactionEntity savedEntity = jpa.save(entity);
        return savedEntity.getId();
    }

    @Override
    public Optional<Transaction> findOne(Long id) {
        return jpa.findById(id).map(entityMapper::toDomainObject);
    }

    @Override
    public List<Transaction> findAll() {
        return jpa.findAll().stream()
                .map(entityMapper::toDomainObject)
                .collect(Collectors.toList());
    }
}
