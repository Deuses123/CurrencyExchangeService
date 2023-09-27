package com.kalibekov.currencyexchangeservice.Data.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.Date;
import java.util.UUID;

@Data
@Table("currency_pairs")
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyPair {
    @Id
    @PrimaryKey
    private String pair;
    private Date createdAt;
}
