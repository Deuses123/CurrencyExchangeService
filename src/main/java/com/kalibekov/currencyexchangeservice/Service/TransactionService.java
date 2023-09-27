package com.kalibekov.currencyexchangeservice.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.DTO.LimitDTO;
import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

public interface TransactionService {
    ResponseEntity<?> createTransaction(TransactionDTO transactionDTO);
    ResponseEntity<?> getTransactionsExceedingLimit(BigDecimal account);

    ResponseEntity<?> createLimit(LimitDTO limitDTO) throws JsonProcessingException;
}
