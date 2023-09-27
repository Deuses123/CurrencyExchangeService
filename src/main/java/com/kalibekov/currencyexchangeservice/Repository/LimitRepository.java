package com.kalibekov.currencyexchangeservice.Repository;

import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface LimitRepository extends JpaRepository<Limit, Long> {
    @Query("SELECT l FROM Limit l WHERE l.sourceAccount = :sourceAccount AND l.limitCategory = :limitCategory ORDER BY l.limitDatetime DESC")
    Optional<List<Limit>> findLimitsBySourceAccountAndLimitCategoryOrderByLimitDatetimeDesc(
            @Param("sourceAccount") BigDecimal sourceAccount,
            @Param("limitCategory") String limitCategory
    );


}
