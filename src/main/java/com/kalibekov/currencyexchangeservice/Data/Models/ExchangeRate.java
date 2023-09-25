package com.kalibekov.currencyexchangeservice.Data.Models;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Table("exchange_rates")
@Data
public class ExchangeRate {

    @Id
    @PrimaryKey
    private UUID id;
    private Date date;
    private String currencyPair;
    private Double rate;
}
