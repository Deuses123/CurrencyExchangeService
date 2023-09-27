package com.kalibekov.currencyexchangeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Repository.LimitRepository;
import com.kalibekov.currencyexchangeservice.Repository.TransactionsRepository;
import com.kalibekov.currencyexchangeservice.Service.TransactionService;
import com.kalibekov.currencyexchangeservice.Tools.CurrencyLimit;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransactionServiceTest {


    @Autowired
    private TransactionService transactionsService;

    @Autowired
    private TransactionsRepository transactionsRepository;

    @Autowired
    private LimitRepository limitRepository;
    @Test
    public void testCreateTransaction() throws ExecutionException, InterruptedException {

        TransactionDTO transaction = new TransactionDTO();
        transaction.setSum(new BigDecimal(1233));
        transaction.setAccountFrom(new BigDecimal(1233));
        transaction.setAccountTo(new BigDecimal(1235));
        transaction.setCurrencyShortname("USD");
        transaction.setDatetime(new Date());
        transaction.setExpenseCategory("products");

        transactionsService.createTransaction(transaction).get();

        List<Transaction> transactionList = transactionsRepository.findTransactionsByAccountFromAndLimitExceeded(transaction.getAccountFrom(), true);

        assertThat(transactionList.size()).isGreaterThan(0);
        Transaction createdTransaction = transactionList.get(0);
        assertThat(createdTransaction.getSum()).isEqualByComparingTo(transaction.getSum());
        assertThat(createdTransaction.getAccountFrom()).isEqualByComparingTo(transaction.getAccountFrom());
        assertThat(createdTransaction.getAccountTo()).isEqualByComparingTo(transaction.getAccountTo());
        assertThat(createdTransaction.getCurrencyShortname()).isEqualTo(transaction.getCurrencyShortname());
    }




    @Test
    public void testTransactionAndLimit() {
        // Создать и сохранить тестовые лимиты и транзакции
        Limit limit = new Limit();
        limit.setLimitCategory("products");
        limit.setLimitAmount(new BigDecimal(1000));
        limit.setSourceAccount(new BigDecimal(12354));
        limit.setCurrency("USD");
        limit.setLimitDatetime(new Date());

        Transaction transaction = new Transaction();
        transaction.setSum(new BigDecimal(1233));
        transaction.setAccountFrom(new BigDecimal(1233));
        transaction.setAccountTo(new BigDecimal(1235));
        transaction.setCurrencyShortname("KZT");
        transaction.setDatetime(new Date());
        transaction.setRemainingLimit(new BigDecimal(0));
        transaction.setLimit(limit);
        transaction.setExpenseCategory("products");
        transaction.setRemainingLimit(new BigDecimal(0));

        limitRepository.save(limit);
        transactionsRepository.save(transaction);

        List<Transaction> transactionList = transactionsRepository.findTransactionsByLimit(limit);

        assertThat(transactionList.size()).isGreaterThan(0);
        Transaction retrievedTransaction = transactionList.get(0);
        assertThat(retrievedTransaction.getSum()).isEqualByComparingTo(transaction.getSum());
        assertThat(retrievedTransaction.getAccountFrom()).isEqualByComparingTo(transaction.getAccountFrom());
        assertThat(retrievedTransaction.getAccountTo()).isEqualByComparingTo(transaction.getAccountTo());
        assertThat(retrievedTransaction.getCurrencyShortname()).isEqualTo(transaction.getCurrencyShortname());
    }

}
