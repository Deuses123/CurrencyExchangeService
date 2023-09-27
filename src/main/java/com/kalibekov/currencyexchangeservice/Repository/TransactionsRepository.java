package com.kalibekov.currencyexchangeservice.Repository;

import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionsRepository extends JpaRepository<Transaction,Long> {
    List<Transaction> findTransactionsByAccountFromAndLimitExceeded(BigDecimal accountFrom, Boolean limitExceeded);
    List<Transaction> findTransactionsByLimit(Limit limit);
}
