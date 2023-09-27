package com.kalibekov.currencyexchangeservice.Data.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LimitDTO {
    private BigDecimal sourceAccount;
    private BigDecimal limitAmount;
    private String limitCategory;
    private String currency;
}
