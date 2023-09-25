package com.kalibekov.currencyexchangeservice.Service;

import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import com.kalibekov.currencyexchangeservice.Repository.LimitRepository;
import com.kalibekov.currencyexchangeservice.Repository.TransactionsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class TransactionsService {
    private final TransactionsRepository transactionsRepository;
    private final LimitRepository limitRepository;
    public ResponseEntity<?> createTransaction(Transaction transaction) {
        try {
            if(transaction.getExpenseCategory().equals("service") || transaction.getExpenseCategory().equals("products")){
                return ResponseEntity.ok("Неправильная категория транзакции");
            }
            Optional<List<Limit>> userLimits =
                    limitRepository.findLimitsBySourceAccountAndLimitCategory(
                            transaction.getAccountFrom(), transaction.getExpenseCategory());

            if(userLimits.isEmpty()){
                Limit limit = Limit.builder()
                        .limitAmount(new BigDecimal(1000).subtract(transaction.getSum()))
                        .limitCategory(transaction.getExpenseCategory())
                        .active(false)
                        .sourceAccount(transaction.getAccountFrom())
                        .build();

                limitRepository.save(limit);
                transaction.setLimit(limit);
                transactionsRepository.save(transaction);
                return ResponseEntity.ok("Операция прошла усепшно");
            }

            Optional<Limit> activeLimit = userLimits.get().stream()
                    .filter(Limit::getActive)
                    .findFirst();
            if(activeLimit.isPresent()){
                transaction...
            }

            return ResponseEntity.status(HttpStatus.CREATED).body(savedTransaction);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка при сохранении транзакции");
        }
    }
}
