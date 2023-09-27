package com.kalibekov.currencyexchangeservice.Service.Impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kalibekov.currencyexchangeservice.Data.DTO.LimitDTO;
import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Repository.LimitRepository;
import com.kalibekov.currencyexchangeservice.Repository.TransactionsRepository;
import com.kalibekov.currencyexchangeservice.Service.CurrencyExchangeService;
import com.kalibekov.currencyexchangeservice.Service.TransactionService;
import com.kalibekov.currencyexchangeservice.Tools.CurrencyLimit;
import com.kalibekov.currencyexchangeservice.Tools.LimitMapper;
import com.kalibekov.currencyexchangeservice.Tools.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {
    private final TransactionsRepository transactionsRepository;
    private final LimitRepository limitRepository;
    private final TransactionMapper transactionMapper;
    private final LimitMapper limitMapper;
    private final CurrencyExchangeService currencyExchangeService;

    @Override
    @Async
    public CompletableFuture<ResponseEntity<?>> createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionMapper.dtoToTransaction(transactionDTO);
        transaction.setDatetime(new Date());

        try {
            String type = transaction.getExpenseCategory();
            if(type.equals("products") || type.equals("services")){

                Optional<Limit> transactionLimit =
                        limitRepository.findLimitsBySourceAccountAndLimitCategoryOrderByLimitDatetimeDesc(
                                transaction.getAccountFrom(), transaction.getExpenseCategory());


                Limit limit = transactionLimit.orElse(
                        Limit.builder()
                                .currency("USD")
                                .limitAmount(new BigDecimal(1000))
                                .limitCategory(transaction.getExpenseCategory())
                                .sourceAccount(transaction.getAccountFrom())
                                .limitDatetime(new Date())
                                .build()
                );

                List<Transaction> transactions;

                if(transactionLimit.isEmpty()){
                    limitRepository.save(limit);
                    transactions = new ArrayList<>();
                }
                else {
                    transactions = transactionsRepository.findTransactionsByLimit(limit);
                }
                transaction.setLimit(limit);
                transactions.add(transaction);

                CurrencyLimit currencyLimit = isLimitExpired(limit, transactions, transaction).get();

                if(currencyLimit.getCurrencyPairNotFound()){
                    return CompletableFuture.completedFuture(ResponseEntity.ok("Пара валют " + transaction.getCurrencyShortname() + "/" + limit.getCurrency() + " не найдено в api"));
                }
                transaction.setLimitExceeded(currencyLimit.getExpired());
                transactionsRepository.save(transaction);
                return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CREATED).body(transaction));
            }
            else {
                return CompletableFuture.completedFuture(ResponseEntity.ok("Неправильная категория транзакции"));
            }
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при сохранении транзакции"));
        }
    }
    @Async
    @Override
    public CompletableFuture<CurrencyLimit> isLimitExpired(Limit limit, List<Transaction> transactions, Transaction transaction) throws JsonProcessingException, ExecutionException, InterruptedException {
        BigDecimal sum = new BigDecimal(0);
        String limitCurrency = limit.getCurrency();

        for (Transaction tr : transactions) {
            String transactionCurrency = tr.getCurrencyShortname();
            if(!limitCurrency.equals(transactionCurrency)){
                CompletableFuture<ExchangeRate> exchangeRate = currencyExchangeService.fetchExchangeRate(limitCurrency+"/"+transactionCurrency);
                if(exchangeRate.get().getRate()!=null){
                    BigDecimal transactionSum = tr.getSum().divide(BigDecimal.valueOf(exchangeRate.get().getRate()), 2);
                    sum = sum.add(transactionSum);
                }
                else {
                    exchangeRate = currencyExchangeService.fetchExchangeRate(transactionCurrency+"/"+limitCurrency);
                    if(exchangeRate.get().getRate()==null){
                        return CompletableFuture.completedFuture(new CurrencyLimit(true, false));
                    }
                    exchangeRate.get().setRate(1/exchangeRate.get().getRate());
                    BigDecimal transactionSum = tr.getSum().divide(BigDecimal.valueOf(exchangeRate.get().getRate()), 2);
                    sum = sum.add(transactionSum);
                }
            }
            else {
                sum = sum.add(tr.getSum());
            }
        }
        transaction.setRemainingLimit(limit.getLimitAmount().subtract(sum));
        return CompletableFuture.completedFuture(new CurrencyLimit(false, sum.compareTo(limit.getLimitAmount()) > 0));
    }

    @Override
    @Async
    public CompletableFuture<ResponseEntity<?>> getTransactionsExceedingLimit(BigDecimal account) {
        List<Transaction> transactions = transactionsRepository.findTransactionsByAccountFromAndLimitExceeded(account, true);
        if(transactions.isEmpty()){
            return CompletableFuture.completedFuture(ResponseEntity.ok("Транзакции превысивших лимит не найдено"));
        }
        return CompletableFuture.completedFuture(ResponseEntity.ok(transactions));
    }


    //Основная валюта USD
    @Override
    @Async
    public CompletableFuture<ResponseEntity<?>> createLimit(LimitDTO limitDTO) throws JsonProcessingException, ExecutionException, InterruptedException {
        if(limitDTO.getLimitAmount().equals(BigDecimal.ZERO)){
            return CompletableFuture.completedFuture(ResponseEntity.ok("Нулевое значение суммы транзакции не принимается"));
        }
        if(limitDTO.getLimitCategory().equals("products") || limitDTO.getLimitCategory().equals("services")){
            Limit limit = limitMapper.dtoToLimit(limitDTO);
            limit.setLimitDatetime(new Date());

            if(!limitDTO.getCurrency().equals("USD")){
                ExchangeRate exchangeRate = currencyExchangeService.fetchExchangeRate("USD/"+limitDTO.getCurrency()).get();
                if (exchangeRate == null) {
                    return CompletableFuture.completedFuture(ResponseEntity.ok("Не правильная валюта"));
                }
            }

            limitRepository.save(limit);
            return CompletableFuture.completedFuture(ResponseEntity.status(HttpStatus.CREATED).body("Лимит успешно создан"));
        }
        else {
            return CompletableFuture.completedFuture(ResponseEntity.ok("Не правильная категория лимта"));
        }
    }

}