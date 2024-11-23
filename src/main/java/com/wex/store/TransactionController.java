package com.wex.store;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController()
public class TransactionController {

    @GetMapping
    public Map<String, String> test (){
        return Map.of("Response", "OK");
    }

}
