package com.ufrn.faulttolerance.exchange.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {
    private final Random random = new Random();

    @GetMapping
    public double getExchangeRate(HttpServletRequest req) {
        System.out.println(LocalDateTime.now().toString() + " " + req.getRemoteAddr());
        if (random.nextDouble() < 0.1) {
            throw new RuntimeException("Simulated Crash");
        }

        return 6.24;
    }
}
