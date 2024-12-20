package com.ufrn.faulttolerance.store.service;

import com.ufrn.faulttolerance.store.model.Product;
import com.ufrn.faulttolerance.store.model.dto.ProductDTO;
import com.ufrn.faulttolerance.store.model.dto.SellDTO;
import com.ufrn.faulttolerance.store.utils.GenerateRandomIdHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ServiceStore {
    private final Random random = new Random();

    private volatile boolean failingProduct = false;
    private long failingSinceProduct = 0;

    private volatile boolean failingSell = false;


    public SellDTO saveSell(ProductDTO productDTO) throws Exception{
        if (!failingSell && random.nextInt(10) == 0) {
            failingSell = true;
        }
        if (failingSell) {
            failingSell = false;
            omissionLoop(); //Fault
        }
        if (productDTO != null && productDTO.getId() != null && !productDTO.getId().isEmpty()) {
            var sellDTO = new SellDTO(GenerateRandomIdHelper.generateRandomId());
            return sellDTO;
        }else{
            throw new Exception();
        }
    }


    public Product getProduct(String id) throws Exception{
        if (!failingProduct && random.nextInt(5) == 0) {
            failingProduct = true;
            failingSinceProduct = System.currentTimeMillis();
        }
        if (failingProduct) {
            if (System.currentTimeMillis() - failingSinceProduct < 5000) {
                throw new Exception();
            } else {
                failingProduct = false;
            }
        }
        if (id != null && !id.isEmpty()) {
            var product = new Product(GenerateRandomIdHelper.generateRandomId(), "Camiseta", 140);
            return product;
        }else{
            throw new Exception();
        }
    }

    void omissionLoop(){
        omissionLoop();
    }
}
