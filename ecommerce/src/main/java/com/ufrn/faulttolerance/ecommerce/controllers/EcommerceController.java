package com.ufrn.faulttolerance.ecommerce.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/ecommerce")
public class EcommerceController {
    private final RestClient restClient;
    private static double lastValidExchangeRate = 7.0;

    public EcommerceController() {
        this.restClient = RestClient.create();
    }

    @GetMapping("/exchange-rate")
    public ResponseEntity<Double> getExchangeRate() {
        String url = "http://exchange:8080/exchange";

        try {
            double rate = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Double.class);
            lastValidExchangeRate = rate;
            return ResponseEntity.ok(rate);
        } catch (RestClientException e) {
            return ResponseEntity.ok(lastValidExchangeRate);
        }
    }
}
