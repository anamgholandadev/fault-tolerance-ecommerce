package com.ufrn.faulttolerance.ecommerce.controllers;

import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ecommerce")
public class EcommerceController {


    @PostMapping("/buy")
    public ResponseEntity<String> buyProduct(@RequestBody ProductBuyDTO productBuy) {
//        var product = ecommerceStoreService.getProductById(productBuy.getProductId());
       return ResponseEntity.ok("code");
    }

}
