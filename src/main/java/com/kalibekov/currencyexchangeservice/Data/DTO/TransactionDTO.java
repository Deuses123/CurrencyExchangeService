package com.kalibekov.currencyexchangeservice.Data.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDTO {
    private BigDecimal accountFrom;
    private BigDecimal accountTo;
    private String expenseCategory;
    private String currencyShortname;
    private BigDecimal sum;
    private Date datetime;
}
