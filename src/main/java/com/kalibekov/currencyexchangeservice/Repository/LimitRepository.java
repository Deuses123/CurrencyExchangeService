package com.kalibekov.currencyexchangeservice.Repository;

import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Long> {
    Optional<List<Limit>> findLimitsBySourceAccount(BigDecimal sourceAccount);
    Optional<List<Limit>> findLimitsBySourceAccountAndLimitCategory(BigDecimal sourceAccount, String limitCategory);
}
