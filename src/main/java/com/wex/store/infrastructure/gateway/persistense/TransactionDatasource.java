package com.wex.store.infrastructure.gateway.persistense;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionDatasource extends ListCrudRepository<TransactionEntity, Long> {

}
