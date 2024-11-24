package com.wex.store.infrastructure.gateway.persistense;

import com.wex.store.domain.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionEntityMapper {

    public TransactionEntity toEntity(Transaction domainObject) {
        TransactionEntity entity = new TransactionEntity();
        entity.setId(domainObject.id());
        entity.setDescription(domainObject.description());
        entity.setDate(domainObject.date());
        entity.setAmount(domainObject.amount());
        return entity;
    }

    public Transaction toDomainObject(TransactionEntity entity) {
        return new Transaction(entity.getId(), entity.getDescription(), entity.getDate(), entity.getAmount());
    }

}
