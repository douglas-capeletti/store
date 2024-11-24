package com.wex.store.infrastructure.gateway.integration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExchangeRateResponseWrapper {

    private List<ExchangeRateResponse> data;

    public List<ExchangeRateResponse> getData() {
        return data;
    }

    public void setData(List<ExchangeRateResponse> data) {
        this.data = data;
    }
}
