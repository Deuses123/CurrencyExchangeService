package com.kalibekov.currencyexchangeservice.Data.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Table("exchange_rates")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExchangeRate {

    @Id
    @PrimaryKey
    private String currencyPair;
    private Date date;
    private Double rate;
}
