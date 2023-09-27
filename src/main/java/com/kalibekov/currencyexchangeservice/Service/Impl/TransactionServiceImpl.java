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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<?> createTransaction(TransactionDTO transactionDTO) {
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

                CurrencyLimit currencyLimit = isLimitExpired(limit, transactions);

                if(currencyLimit.getCurrencyPairNotFound()){
                    return ResponseEntity.ok("Пара валют " + transaction.getCurrencyShortname() + "/" + limit.getCurrency() + " не найдено в api");
                }
                transaction.setLimitExceeded(currencyLimit.getExpired());
                transactionsRepository.save(transaction);
                return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
            }
            else {
                return ResponseEntity.ok("Неправильная категория транзакции");
            }
        }
        catch (Exception e) {
            log.info(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при сохранении транзакции");
        }
    }
    private CurrencyLimit isLimitExpired(Limit limit, List<Transaction> transactions) throws JsonProcessingException {
        BigDecimal sum = new BigDecimal(0);
        String limitCurrency = limit.getCurrency();

        for (Transaction tr : transactions) {
            String transactionCurrency = tr.getCurrencyShortname();
            if(!limitCurrency.equals(transactionCurrency)){
                ExchangeRate exchangeRate = currencyExchangeService.fetchExchangeRate(limitCurrency+"/"+transactionCurrency);
                if(exchangeRate.getRate()!=null){
                    BigDecimal transactionSum = tr.getSum().divide(BigDecimal.valueOf(exchangeRate.getRate()), 2);
                    sum = sum.add(transactionSum);
                }
                else {
                    exchangeRate = currencyExchangeService.fetchExchangeRate(transactionCurrency+"/"+limitCurrency);
                    if(exchangeRate.getRate()==null){
                        return new CurrencyLimit(true, false);
                    }
                    exchangeRate.setRate(1/exchangeRate.getRate());
                    BigDecimal transactionSum = tr.getSum().divide(BigDecimal.valueOf(exchangeRate.getRate()), 2);
                    sum = sum.add(transactionSum);
                }
            }
            else {
                sum = sum.add(tr.getSum());
            }
        }
        return new CurrencyLimit(false, sum.compareTo(limit.getLimitAmount()) > 0);
    }

    @Override
    public ResponseEntity<?> getTransactionsExceedingLimit(BigDecimal account) {
        List<Transaction> transactions = transactionsRepository.findTransactionsByAccountFromAndLimitExceeded(account, true);
        if(transactions.isEmpty()){
            return ResponseEntity.ok("Транзакции превысивших лимит не найдено");
        }
        return ResponseEntity.ok(transactions);
    }


    @Override
    //Основная валюта USD
    public ResponseEntity<?> createLimit(LimitDTO limitDTO) throws JsonProcessingException {
        if(limitDTO.getLimitAmount().equals(BigDecimal.ZERO)){
            return ResponseEntity.ok("Нулевое значение суммы транзакции не принимается");
        }
        if(limitDTO.getLimitCategory().equals("products") || limitDTO.getLimitCategory().equals("services")){
            Limit limit = limitMapper.dtoToLimit(limitDTO);
            limit.setLimitDatetime(new Date());

            if(!limitDTO.getCurrency().equals("USD")){
                ExchangeRate exchangeRate = currencyExchangeService.fetchExchangeRate("USD/"+limitDTO.getCurrency());
                if (exchangeRate == null) {
                    return ResponseEntity.ok("Не правильная валюта");
                }
            }

            limitRepository.save(limit);
            return ResponseEntity.status(HttpStatus.CREATED).body("Лимит успешно создан");
        }
        else {
            return ResponseEntity.ok("Не правильная категория лимта");
        }
    }


}
