package com.ufrn.faulttolerance.store.controllers;

import com.ufrn.faulttolerance.store.dto.ProductDTO;
import com.ufrn.faulttolerance.store.dto.SellDTO;
import com.ufrn.faulttolerance.store.model.Product;
import com.ufrn.faulttolerance.store.utils.GenerateRandomIdHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    @PostMapping("/sell")
    public ResponseEntity<SellDTO> sell(@RequestBody ProductDTO  productDTO) {
        if (productDTO != null && productDTO.getId() != null && !productDTO.getId().isEmpty() ) {
            var sellDTO = new SellDTO(GenerateRandomIdHelper.generateRandomId());
            return ResponseEntity.ok(sellDTO);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Product> product(@PathVariable String  id) {
        if (id != null && !id.isEmpty()) {
            var product = new Product(GenerateRandomIdHelper.generateRandomId(), "Camiseta", 140.60);
            return ResponseEntity.ok(product);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

}
