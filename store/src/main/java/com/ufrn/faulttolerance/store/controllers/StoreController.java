package com.ufrn.faulttolerance.store.controllers;

import com.ufrn.faulttolerance.store.model.dto.ProductDTO;
import com.ufrn.faulttolerance.store.model.dto.SellDTO;
import com.ufrn.faulttolerance.store.model.Product;
import com.ufrn.faulttolerance.store.service.ServiceStore;
import com.ufrn.faulttolerance.store.utils.GenerateRandomIdHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/store")
public class StoreController {

    @Autowired
    private ServiceStore serviceStore;

    @PostMapping("/sell")
    public ResponseEntity<SellDTO> sell(@RequestBody ProductDTO productDTO) {
        try {
            SellDTO sellDTO = serviceStore.saveSell(productDTO);
            return ResponseEntity.ok(sellDTO);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> product(@PathVariable String id) {
        try {
            Product product = serviceStore.getProduct(id);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
