package com.kalibekov.currencyexchangeservice.Service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.DTO.LimitDTO;
import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Tools.CurrencyLimit;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public interface TransactionService {
    CompletableFuture<ResponseEntity<?>> createTransaction(TransactionDTO transactionDTO);
    CompletableFuture<ResponseEntity<?>> getTransactionsExceedingLimit(BigDecimal account);

    CompletableFuture<ResponseEntity<?>> createLimit(LimitDTO limitDTO) throws JsonProcessingException, ExecutionException, InterruptedException;
    CompletableFuture<CurrencyLimit> isLimitExpired(Limit limit, List<Transaction> transactions, Transaction transaction) throws JsonProcessingException, ExecutionException, InterruptedException ;
}
