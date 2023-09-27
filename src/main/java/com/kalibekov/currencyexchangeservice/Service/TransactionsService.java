package com.kalibekov.currencyexchangeservice.Service;

import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Repository.LimitRepository;
import com.kalibekov.currencyexchangeservice.Repository.TransactionsRepository;
import com.kalibekov.currencyexchangeservice.Tools.TransactionMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final LimitRepository limitRepository;
    private final TransactionMapper transactionMapper;
    public ResponseEntity<?> createTransaction(TransactionDTO transactionDTO) {
        Transaction transaction = transactionMapper.dtoToTransaction(transactionDTO);
        try {
            String type = transaction.getExpenseCategory();
            if(type.equals("products") || type.equals("service")){

                Optional<List<Limit>> userLimits =
                        limitRepository.findLimitsBySourceAccountAndLimitCategoryOrderByLimitDatetimeDesc(
                                transaction.getAccountFrom(), transaction.getExpenseCategory());
                //В аккаунте не было лимитов
                if(userLimits.orElseThrow().isEmpty()){
                    Limit limit = Limit.builder()
                            .limitAmount(new BigDecimal(1000).subtract(transaction.getSum()))
                            .limitCategory(transaction.getExpenseCategory())
                            .sourceAccount(transaction.getAccountFrom())
                            .build();

                    limitRepository.save(limit);
                    transaction.setLimit(limit);
                    transactionsRepository.save(transaction);
                    return ResponseEntity.status(HttpStatus.CREATED).body("Операция прошла усепшно");
                }

                //В аккаунте есть лимиты
                Limit newestLimit = Collections.max(userLimits.get(), Comparator.comparing(Limit::getLimitDatetime));
                Limit limit = Limit.builder()
                        .sourceAccount(transaction.getAccountFrom())
                        .limitCategory(newestLimit.getLimitCategory())
                        .limitAmount(newestLimit.getLimitAmount().subtract(transaction.getSum()))
                        .limitDatetime(new Date())
                        .build();
                if(limit.getLimitAmount().compareTo(BigDecimal.ZERO)<0){
                    transaction.setLimitExceeded(true);
                }
                limitRepository.save(limit);
                transaction.setLimit(limit);
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
}
