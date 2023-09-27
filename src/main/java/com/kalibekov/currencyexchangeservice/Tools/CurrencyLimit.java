package com.kalibekov.currencyexchangeservice.Tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyLimit {
    private Boolean currencyPairNotFound;
    private Boolean expired;
}

