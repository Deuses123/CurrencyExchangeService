package com.kalibekov.currencyexchangeservice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Repository.LimitRepository;
import com.kalibekov.currencyexchangeservice.Service.CurrencyExchangeService;
import com.kalibekov.currencyexchangeservice.Service.Impl.TransactionServiceImpl;
import com.kalibekov.currencyexchangeservice.Service.TransactionService;
import com.kalibekov.currencyexchangeservice.Tools.CurrencyLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LimitTest {

    private TransactionService transactionsService;

    @Mock
    private LimitRepository limitRepository;

    @Mock
    private CurrencyExchangeService currencyExchangeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionsService = new TransactionServiceImpl(null, limitRepository, null, null, currencyExchangeService);
    }

    @Test
    void testIsLimitExpired_LimitExceeded() throws ExecutionException, InterruptedException, JsonProcessingException {
        // Arrange
        Limit limit = new Limit();
        limit.setCurrency("USD");
        limit.setLimitAmount(new BigDecimal(1000));
        limit.setLimitCategory("products");
        limit.setSourceAccount(new BigDecimal(12345));

        Transaction transaction1 = new Transaction();
        transaction1.setSum(new BigDecimal(600));
        transaction1.setCurrencyShortname("USD");
        transaction1.setExpenseCategory("products");

        Transaction transaction2 = new Transaction();
        transaction2.setSum(new BigDecimal(500));
        transaction2.setCurrencyShortname("USD");
        transaction1.setExpenseCategory("products");

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction1);
        transactions.add(transaction2);

        when(limitRepository.findLimitsBySourceAccountAndLimitCategoryOrderByLimitDatetimeDesc(limit.getSourceAccount(), limit.getLimitCategory()))
                .thenReturn(java.util.Optional.of(limit));


        // Act
        CompletableFuture<CurrencyLimit> result = transactionsService.isLimitExpired(limit, transactions, transaction1);

        // Assert
        assertTrue(result.get().getExpired());
        assertFalse(result.get().getCurrencyPairNotFound());
    }



}
