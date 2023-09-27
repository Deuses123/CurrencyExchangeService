package com.kalibekov.currencyexchangeservice.Tools;

import com.kalibekov.currencyexchangeservice.Data.DTO.LimitDTO;
import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.Limit;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LimitMapper {
    Limit dtoToLimit(LimitDTO limitDTO);
}
