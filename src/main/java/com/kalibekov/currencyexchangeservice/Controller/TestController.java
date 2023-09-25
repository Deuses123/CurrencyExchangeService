package com.kalibekov.currencyexchangeservice.Controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Example API", description = "Описание вашего API")
@RequestMapping("/api")
public class TestController {

    @PostMapping("/create-user")
    @Operation(summary = "Приветствие", description = "Возвращает приветственное сообщение")
    public String createUser(
            @RequestParam(name = "name", defaultValue = "Мир") @Parameter(description = "Имя для приветствия") String name
    ){
        return name;
    }

    @GetMapping("/get-user")
    public String getUser(@RequestParam String name){
        return name;
    }
}
