package com.kalibekov.currencyexchangeservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import org.springframework.http.ResponseEntity;

public interface CurrencyExchangeService {
    ExchangeRate fetchExchangeRate(String currencyPair) throws JsonProcessingException;
}
