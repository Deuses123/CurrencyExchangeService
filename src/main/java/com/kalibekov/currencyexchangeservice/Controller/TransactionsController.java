package com.kalibekov.currencyexchangeservice.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.DTO.LimitDTO;
import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
@Tag(name = "Управление транзакциями", description = "В контроллере можно создавать лимиты и отправлять транзакции")
public class TransactionsController {
    private final TransactionService transactionsService;

    @GetMapping("/get-transactions-exceeding-limit")
    @Operation(summary = "Метод для получения транзакции, превысившие лимит")
    public ResponseEntity<?> getTransactionsExceedingLimit(@RequestParam(name = "account", defaultValue = "123456") BigDecimal account){
        try {
            return transactionsService.getTransactionsExceedingLimit(account).get();
        }
        catch (Exception e){
            log.info(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create-limit")
    @Operation(summary = "Метод для создания лимита. limitCategory должен быть services || products")
    public ResponseEntity<?> createLimit(@RequestBody LimitDTO limitDTO) throws JsonProcessingException {
        try {
            return transactionsService.createLimit(limitDTO).get();
        }
        catch (Exception e){
            log.info(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/create-transaction")
    @Operation(summary = "Метод для сохранения транзакции. expenseCategory должен быть services || products")
    public ResponseEntity<?> saveTransaction(@RequestBody TransactionDTO transaction) {
        try {
            return transactionsService.createTransaction(transaction).get();
        }
        catch (Exception e){
            log.info(String.valueOf(e));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
