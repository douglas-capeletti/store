package com.wex.store.infrastructure.gateway.integration;

import com.wex.store.domain.ExchangeRate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.text.MessageFormat;
import java.time.LocalDate;
import java.util.Optional;

@Component
public class ExchangeRateClient {

    private final RestTemplate restTemplate;
    @Value("${integration.client.exchange-rate.url}")
    private String GET_EXCHANGE_BASE_URL;
    @Value("${integration.client.exchange-rate.filter}")
    private String GET_EXCHANGE_FILTER;

    public ExchangeRateClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Optional<ExchangeRate> getExchange(String country, LocalDate date) {
        String requestedFields = "?fields=record_date,exchange_rate,country,currency";
        String encodedCountryName = URLEncoder.encode(country, Charset.defaultCharset()).replaceAll("\\+", "%20");
        String requestedFilters = MessageFormat.format(GET_EXCHANGE_FILTER, encodedCountryName, date.toString());
        URI uri = URI.create(GET_EXCHANGE_BASE_URL + requestedFields + requestedFilters);
        ExchangeRateResponseWrapper response = restTemplate.getForObject(uri, ExchangeRateResponseWrapper.class);
        if (response == null || response.getData() == null || response.getData().isEmpty()) {
            return Optional.empty();
        }
        ExchangeRateResponse responseData = response.getData().get(0);
        return Optional.of(new ExchangeRate(responseData.getCountry(), responseData.getDate(), responseData.getRate(), responseData.getCurrency()));
    }
}
