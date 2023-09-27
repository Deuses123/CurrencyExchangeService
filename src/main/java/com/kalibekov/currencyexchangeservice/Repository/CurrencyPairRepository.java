package com.kalibekov.currencyexchangeservice.Repository;

import com.kalibekov.currencyexchangeservice.Data.Models.CurrencyPair;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

public interface CurrencyPairRepository extends CassandraRepository<CurrencyPair, UUID> {

}
