package com.kalibekov.currencyexchangeservice.Repository;

import com.kalibekov.currencyexchangeservice.Data.Models.ExchangeRate;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ExchangeRateRepository extends CassandraRepository<ExchangeRate, UUID> {
}
