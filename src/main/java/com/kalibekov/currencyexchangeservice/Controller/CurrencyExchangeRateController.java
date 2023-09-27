package com.kalibekov.currencyexchangeservice.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Service.CurrencyExchangeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/currency-exchange-rate")
@Tag(name = "Курс валют", description = "Пример USD/KZT, RUB/KZT")
public class CurrencyExchangeRateController {

    private final CurrencyExchangeService currencyExchangeService;

    @GetMapping("/convert")
    @Operation(summary = "Метод для конвертации валют")
    public ResponseEntity<?> convert(@RequestParam(name = "currencyPair", defaultValue = "USD/KZT") String currencyPair) throws JsonProcessingException, ExecutionException, InterruptedException {
        return ResponseEntity.ok(currencyExchangeService.fetchExchangeRate(currencyPair).get());
    }


}
