package com.ufrn.faulttolerance.store.controllers;

import com.ufrn.faulttolerance.store.model.dto.ProductDTO;
import com.ufrn.faulttolerance.store.model.dto.SellDTO;
import com.ufrn.faulttolerance.store.model.Product;
import com.ufrn.faulttolerance.store.service.ServiceStore;
import com.ufrn.faulttolerance.store.utils.GenerateRandomIdHelper;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Random;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private ServiceStore serviceStore;

    @PostMapping("/sell")
    public ResponseEntity<SellDTO> sell(@RequestBody ProductDTO productDTO, HttpServletRequest req) {
        System.out.println(LocalDateTime.now().toString() + " " + req.getRemoteAddr());
        try {
            SellDTO sellDTO = serviceStore.saveSell(productDTO);
            return ResponseEntity.ok(sellDTO);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> product(@PathVariable String id, HttpServletRequest req) {
        System.out.println(LocalDateTime.now().toString() + " " + req.getRemoteAddr());
        Product product = serviceStore.getProduct(id);
        return ResponseEntity.ok(product);
    }
}
