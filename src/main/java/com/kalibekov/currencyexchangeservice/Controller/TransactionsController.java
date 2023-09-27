package com.kalibekov.currencyexchangeservice.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.DTO.LimitDTO;
import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionsController {
    private final TransactionService transactionsService;


    @GetMapping("/get-transactions-exceeding-limit")
    public ResponseEntity<?> getTransactionsExceedingLimit(@RequestParam BigDecimal account){
        return transactionsService.getTransactionsExceedingLimit(account);
    }
    @PostMapping("/create-limit")
    public ResponseEntity<?> createLimit(@RequestBody LimitDTO limitDTO) throws JsonProcessingException {
        return transactionsService.createLimit(limitDTO);
    }
    @PostMapping("/create-transaction")
    public ResponseEntity<?> saveTransaction(@RequestBody TransactionDTO transaction) {
        return transactionsService.createTransaction(transaction);
    }
}
