package com.kalibekov.currencyexchangeservice.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kalibekov.currencyexchangeservice.Data.Models.CurrencyPair;
import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import com.kalibekov.currencyexchangeservice.Repository.CurrencyPairRepository;
import com.kalibekov.currencyexchangeservice.Repository.ExchangeRateRepository;
import com.kalibekov.currencyexchangeservice.Service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private final ExchangeRateRepository exchangeRateRepository;
    private final RestTemplate restTemplate = new RestTemplate();
    private final CurrencyPairRepository currencyPairRepository;

    @Value("${spring.twelvedata.apikey}")
    private String apiKey;



    // Запускать каждый день в полночь @Scheduled(cron = "0 0 * * *")
    @Scheduled(cron = "0 0 */24 * * *")
    public void fetchExchange() throws JsonProcessingException {
        List<CurrencyPair> currencyPairList = currencyPairRepository.findAll();
        for(CurrencyPair currencyPair: currencyPairList){
            getExchangeRateFromAPI(currencyPair.getPair());
        }
        log.info("Обновление курса валют");
    }



    //todo: Метод проверяет с бд валюту и если его там нет то получает его из апи
    @Override
    public ExchangeRate fetchExchangeRate(String currencyPair) throws JsonProcessingException {

        List<ExchangeRate> exchangeRates = exchangeRateRepository
                .findExchangeRatesByCurrencyPair(currencyPair);

        if(exchangeRates.isEmpty()){
            CurrencyPair currencyPair1 = new CurrencyPair();
            currencyPair1.setPair(currencyPair);
            currencyPair1.setCreatedAt(new Date());
            currencyPairRepository.save(currencyPair1);

            return getExchangeRateFromAPI(currencyPair);
        }
        return exchangeRates.stream()
                .max(Comparator.comparing(ExchangeRate::getDate))
                .orElse(null);
    }


    //todo:Метод получает курс валют с twelvedata и сохраняет в бд
    public ExchangeRate getExchangeRateFromAPI(String currencyPair) throws JsonProcessingException {
        if(currencyPair.contains("/") && currencyPair.length() == 7) {
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
                    exchangeRate.setCurrencyPair(currencyPair);
                    exchangeRate.setDate(new Date());
                    exchangeRate.setRate(Double.parseDouble(a.get("rate").toString()));
                    exchangeRateRepository.save(exchangeRate);
                }
                else {
                    log.info(currencyPair+" не нашелся");
                }
            }
            return exchangeRate;
        }
        return null;
    }


}