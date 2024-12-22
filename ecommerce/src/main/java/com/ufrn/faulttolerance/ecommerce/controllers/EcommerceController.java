package com.ufrn.faulttolerance.ecommerce.controllers;

import com.ufrn.faulttolerance.ecommerce.model.Product;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.SellDTO;
import com.ufrn.faulttolerance.ecommerce.services.EcommerceService;

import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

@RestController
@RequestMapping("/ecommerce")
public class EcommerceController {
  @Autowired
  private EcommerceService ecommerceService;

  @PostMapping("/buy")
  public ResponseEntity<Void> buyProduct(@RequestBody ProductBuyDTO productBuyDTO) {
    try {
      ecommerceService.buyProduct(productBuyDTO);
      return ResponseEntity.ok().build();
    } catch (Exception e) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}