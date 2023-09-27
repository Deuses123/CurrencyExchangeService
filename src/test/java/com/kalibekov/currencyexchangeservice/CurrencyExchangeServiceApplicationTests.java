package com.kalibekov.currencyexchangeservice;

import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Repository.LimitRepository;
import com.kalibekov.currencyexchangeservice.Repository.TransactionsRepository;
import com.kalibekov.currencyexchangeservice.Service.Impl.TransactionServiceImpl;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@RunWith(SpringRunner.class)
@SpringBootTest
class CurrencyExchangeServiceApplicationTests {

    @InjectMocks
    private TransactionServiceImpl transactionService;
    @Mock
    private LimitRepository limitRepository;

    @Mock
    private TransactionsRepository transactionsRepository;


    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }





}
