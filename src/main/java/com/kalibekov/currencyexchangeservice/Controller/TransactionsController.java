package com.kalibekov.currencyexchangeservice.Controller;

import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Service.TransactionsService;
import com.kalibekov.currencyexchangeservice.Tools.TransactionMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionsController {
    private final TransactionsService transactionsService;

    @PostMapping("/create-transaction")
    public ResponseEntity<?> saveTransaction(@RequestBody TransactionDTO transaction) {
        return transactionsService.createTransaction(transaction);
    }
}
