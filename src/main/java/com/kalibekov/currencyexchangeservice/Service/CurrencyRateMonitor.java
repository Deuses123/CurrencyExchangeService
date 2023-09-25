package com.kalibekov.currencyexchangeservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import com.kalibekov.currencyexchangeservice.Repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
@RequiredArgsConstructor
public class CurrencyRateMonitor {

    private final CurrencyExchangeService currencyExchangeService;


    @Scheduled(fixedRate = 36000000)
    public void fetchCurrencyRate() throws JsonProcessingException {
//        currencyExchangeService.reloadExchangeRate("USD/RUB");
//        currencyExchangeService.reloadExchangeRate("USD/KZT");
    }

    public void fetchCurrencyRate(String pair) throws JsonProcessingException {
        if(pair.contains("/") && pair.length() == 7)
            currencyExchangeService.reloadExchangeRate(pair);
    }


}
