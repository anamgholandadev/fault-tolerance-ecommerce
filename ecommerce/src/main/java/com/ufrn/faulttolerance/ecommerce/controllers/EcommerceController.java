package com.ufrn.faulttolerance.ecommerce.controllers;

import com.ufrn.faulttolerance.ecommerce.model.Product;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.SellDTO;
import com.ufrn.faulttolerance.ecommerce.services.EcommerceService;
import org.springframework.beans.factory.annotation.Autowired;
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
    } catch (RestClientException e) {
      ResponseEntity.internalServerError();
    } catch (Throwable e) {
      ResponseEntity.internalServerError();
    }
    return ResponseEntity.ok().build();
  }

}