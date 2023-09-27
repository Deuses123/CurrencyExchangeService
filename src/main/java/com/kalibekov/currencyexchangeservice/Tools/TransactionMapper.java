package com.kalibekov.currencyexchangeservice.Tools;

import com.kalibekov.currencyexchangeservice.Data.DTO.TransactionDTO;
import com.kalibekov.currencyexchangeservice.Data.Models.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    Transaction dtoToTransaction(TransactionDTO transactionDTO);
}
