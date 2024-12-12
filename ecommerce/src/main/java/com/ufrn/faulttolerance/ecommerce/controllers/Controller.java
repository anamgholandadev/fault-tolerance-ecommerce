package com.ufrn.faulttolerance.ecommerce.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

@RestController
@RequestMapping("/hello")
public class Controller {
    RestClient restClient;

    public Controller() {
        this.restClient = RestClient.create();
    }

    @GetMapping
    public String helloWorld() {
        return restClient.get().uri("http://store:8080/hello").retrieve().body(String.class);
    }

}
