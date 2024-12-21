package com.ufrn.faulttolerance.store.service;

import com.ufrn.faulttolerance.store.model.Product;
import com.ufrn.faulttolerance.store.model.dto.ProductDTO;
import com.ufrn.faulttolerance.store.model.dto.SellDTO;
import com.ufrn.faulttolerance.store.utils.GenerateRandomIdHelper;
import com.ufrn.faulttolerance.store.utils.FailureSimulator;
import org.springframework.stereotype.Service;

@Service
public class ServiceStore {

    private final FailureSimulator failureSimulatorGetProduct;
    private final FailureSimulator failureSimulatorSell;

    public ServiceStore() {
        this.failureSimulatorGetProduct = new FailureSimulator(0.2, 0);
        this.failureSimulatorSell = new FailureSimulator(0.1, 5000);
    }


    public SellDTO saveSell(ProductDTO productDTO) throws Exception {
        if (failureSimulatorSell.shouldTriggerFailure()) {
            failureSimulatorSell.activateFailure();
        }
        if (failureSimulatorSell.isFailureActive()) {
            throw new Exception();
        }
        var sellDTO = new SellDTO(GenerateRandomIdHelper.generateRandomId());
        return sellDTO;
    }


    public Product getProduct(String id)  {
        if (failureSimulatorGetProduct.shouldTriggerFailure()) {
            failureSimulatorGetProduct.activateFailure();
        }
        if (failureSimulatorGetProduct.isFailureActive()) {
            while(true){}
        }
        var product = new Product(id, "Camiseta", 140);
        return product;
    }

}
