package com.wex.store;

import com.wex.store.application.gateway.ExchangeRateGateway;
import com.wex.store.application.gateway.TransactionGateway;
import com.wex.store.application.usecase.TransactionInteractor;
import com.wex.store.infrastructure.gateway.ExchangeRateClientGateway;
import com.wex.store.infrastructure.gateway.TransactionRepositoryGateway;
import com.wex.store.infrastructure.gateway.integration.ExchangeRateClient;
import com.wex.store.infrastructure.gateway.persistense.TransactionDatasource;
import com.wex.store.infrastructure.gateway.persistense.TransactionEntityMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class})
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public TransactionGateway transactionGateway(TransactionDatasource transactionDatasource, TransactionEntityMapper transactionEntityMapper) {
        return new TransactionRepositoryGateway(transactionDatasource, transactionEntityMapper);
    }

    @Bean
    public ExchangeRateGateway exchangeRateGateway(ExchangeRateClient exchangeRateClient) {
        return new ExchangeRateClientGateway(exchangeRateClient);
    }

    @Bean
    public TransactionInteractor transactionInteractor(TransactionGateway transactionGateway, ExchangeRateGateway exchangeRateGateway) {
        return new TransactionInteractor(transactionGateway, exchangeRateGateway);
    }

}
