package com.kalibekov.currencyexchangeservice.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import com.kalibekov.currencyexchangeservice.Repository.ExchangeRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CurrencyExchangeService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${spring.twelvedata.apikey}")
    private String apiKey;

    public void reloadExchangeRate(
            String currencyPair) throws JsonProcessingException {

        ExchangeRate exchangeRate = new ExchangeRate();

        String apiUrl = "https://api.twelvedata.com/currency_conversion?"
                + "&symbol=" + currencyPair
                + "&apikey=" + apiKey;

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(apiUrl, String.class);
        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            var jsonResponse = responseEntity.getBody();
            ObjectMapper objectMapper = new ObjectMapper();

            HashMap<String, Object> a = (HashMap<String, Object>) objectMapper.readValue(jsonResponse, HashMap.class);


            if(a.get("rate")!=null) {
                exchangeRate.setId(UUID.randomUUID());
                exchangeRate.setCurrencyPair(currencyPair);
                exchangeRate.setDate(new Date());
                exchangeRate.setRate(Double.parseDouble(a.get("rate").toString()));
                exchangeRateRepository.save(exchangeRate);
            }
        }
    }
}