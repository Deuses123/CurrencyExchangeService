package com.kalibekov.currencyexchangeservice.Repository;

import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import org.springframework.data.cassandra.repository.AllowFiltering;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExchangeRateRepository extends CassandraRepository<ExchangeRate, String> {

    List<ExchangeRate> findExchangeRatesByCurrencyPair(String currencyPair);
}
