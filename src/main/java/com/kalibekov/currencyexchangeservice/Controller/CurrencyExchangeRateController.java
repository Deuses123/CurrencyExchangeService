package com.kalibekov.currencyexchangeservice.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Service.CurrencyExchangeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/currency-exchange-rate")
public class CurrencyExchangeRateController {

    private final CurrencyExchangeService currencyExchangeService;

    @GetMapping("/convert")
    public ResponseEntity<?> convert(@RequestParam String currencyPair) throws JsonProcessingException {
        return ResponseEntity.ok(currencyExchangeService.fetchExchangeRate(currencyPair));
    }
}
