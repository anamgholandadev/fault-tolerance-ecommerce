package com.ufrn.faulttolerance.exchange.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    private final Random random = new Random();

    @GetMapping
    public double getExchangeRate() {
        if (random.nextDouble() < 0.1) {
            throw new RuntimeException("Simulated Crash");
        }

        return 6.24;
    }
}
