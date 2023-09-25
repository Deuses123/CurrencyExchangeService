package com.kalibekov.currencyexchangeservice.Tools;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
    private static ObjectMapper objectMapper = get();

    private static ObjectMapper get(){
        return new ObjectMapper();
    }

    public static JsonNode parse(String asc) throws JsonProcessingException {
        return objectMapper.readTree(asc);
    }
}
