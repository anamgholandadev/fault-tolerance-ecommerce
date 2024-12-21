package com.ufrn.faulttolerance.ecommerce.controllers;

import com.ufrn.faulttolerance.ecommerce.model.Product;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import com.ufrn.faulttolerance.ecommerce.services.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ecommerce")
public class EcommerceController {
  @Autowired
  private EcommerceService ecommerceService;

  @PostMapping("/buy")
  public ResponseEntity<Product> buyProduct(@RequestBody ProductBuyDTO productBuyDTO) {
    var product = ecommerceService.buyProduct(productBuyDTO);
    return ResponseEntity.ok(product);
  }

  @GetMapping("/exchange-rate")
  public ResponseEntity<Double> getExchangeRate() {
    var exchangeRate = ecommerceService.getExchangeRate();

    return ResponseEntity.ok(exchangeRate);
  }

}