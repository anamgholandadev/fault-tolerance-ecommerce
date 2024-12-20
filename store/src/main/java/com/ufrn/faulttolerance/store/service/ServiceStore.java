package com.ufrn.faulttolerance.store.service;

import com.ufrn.faulttolerance.store.model.Product;
import com.ufrn.faulttolerance.store.model.dto.ProductDTO;
import com.ufrn.faulttolerance.store.model.dto.SellDTO;
import com.ufrn.faulttolerance.store.utils.GenerateRandomIdHelper;
import com.ufrn.faulttolerance.store.utils.FailureSimulator;
import org.springframework.stereotype.Service;

@Service
public class ServiceStore {

    private final FailureSimulator timeFailureSimulatorGetProduct;
    private final FailureSimulator timeFailureSimulatorSell;

    public ServiceStore() {
        this.timeFailureSimulatorGetProduct = new FailureSimulator(0.2, 0);
        this.timeFailureSimulatorSell = new FailureSimulator(0.1, 5000);
    }


    public SellDTO saveSell(ProductDTO productDTO) throws Exception {
        if (timeFailureSimulatorSell.shouldTriggerFailure()) {
            timeFailureSimulatorSell.activateFailure();
        }
        if (timeFailureSimulatorSell.isFailureActive()) {
            throw new Exception();
        }
        var sellDTO = new SellDTO(GenerateRandomIdHelper.generateRandomId());
        return sellDTO;
    }


    public Product getProduct(String id)  {
        if (timeFailureSimulatorGetProduct.shouldTriggerFailure()) {
            timeFailureSimulatorGetProduct.activateFailure();
        }
        if (timeFailureSimulatorGetProduct.isFailureActive()) {
            while(true){}
        }
        var product = new Product(GenerateRandomIdHelper.generateRandomId(), "Camiseta", 140);
        return product;
    }

}
