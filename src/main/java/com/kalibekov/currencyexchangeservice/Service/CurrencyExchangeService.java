package com.kalibekov.currencyexchangeservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;

import java.util.concurrent.CompletableFuture;

public interface CurrencyExchangeService {
    CompletableFuture<ExchangeRate> fetchExchangeRate(String currencyPair) throws JsonProcessingException;
    void fetchExchange() throws JsonProcessingException;
}
