package com.kalibekov.currencyexchangeservice.Tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.Models.CurrencyPair;
import com.kalibekov.currencyexchangeservice.Service.CurrencyExchangeService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class InitConstruction {

    private final CurrencyExchangeService currencyExchangeService;

    @PostConstruct
    public void init() throws JsonProcessingException {
        currencyExchangeService.fetchExchangeRate("USD/KZT");
        currencyExchangeService.fetchExchangeRate("USD/RUB");
    }

}
