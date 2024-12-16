package com.ufrn.faulttolerance.store.controllers;

import com.ufrn.faulttolerance.store.model.dto.ProductDTO;
import com.ufrn.faulttolerance.store.model.dto.SellDTO;
import com.ufrn.faulttolerance.store.model.Product;
import com.ufrn.faulttolerance.store.utils.GenerateRandomIdHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final Random random = new Random();

    private volatile boolean failingProduct = false;
    private long failingSinceProduct = 0;

    private volatile boolean failingSell = false;
    @GetMapping
    public String helloWorld() {
        return "Hello From Fidelity!";
    }

    @PostMapping("/sell")
    public ResponseEntity<SellDTO> sell(@RequestBody ProductDTO productDTO) {
        if (!failingSell && random.nextInt(10) == 0) {
            failingSell = true;
        }
        if (failingSell) {
            failingSell = false;
            omissionLoop(); //Fault
        }

        if (productDTO != null && productDTO.getId() != null && !productDTO.getId().isEmpty()) {
            var sellDTO = new SellDTO(GenerateRandomIdHelper.generateRandomId());
            return ResponseEntity.ok(sellDTO);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> product(@PathVariable String id) {
        if (!failingProduct && random.nextInt(5) == 0) {
            failingProduct = true;
            failingSinceProduct = System.currentTimeMillis();
        }
        if (failingProduct) {
            if (System.currentTimeMillis() - failingSinceProduct < 5000) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                failingProduct = false;
            }
        }
        if (id != null && !id.isEmpty()) {
            var product = new Product(GenerateRandomIdHelper.generateRandomId(), "Camiseta", 140.60);
            return ResponseEntity.ok(product);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    void omissionLoop(){
        omissionLoop();
    }
}
