package com.ufrn.faulttolerance.ecommerce.services;

import com.ufrn.faulttolerance.ecommerce.model.Product;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.SellDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.http.MediaType;

@Service
public class EcommerceService {

    private static double lastValidExchangeRate = 7.0;

    private  RestClient restClient;

    public EcommerceService() {
        this.restClient = RestClient.create();
    }

    public Product buyProduct(ProductBuyDTO productBuyDTO) throws RestClientException {
        Product product = getProduct(productBuyDTO.getProductId());
        double rate = getExchangeRate();
        SellDTO sellDTO = sellProduct(productBuyDTO);
        return product;

    }

    public Double getExchangeRate() {
        String url = "http://localhost:8081/exchange";
        try {
            double rate = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Double.class);
            lastValidExchangeRate = rate;
            return rate;
        } catch (RestClientException e) {
            return lastValidExchangeRate;
        }
    }

    public Product getProduct(String productId) {
        String url = "http://localhost:8083/store/product/" + productId;
        try {
            Product product = restClient.get()
                    .uri(url)
                    .retrieve()
                    .body(Product.class);
            return product;
        } catch (RestClientException e) {
            return new Product();
        }
    }

    public SellDTO sellProduct(ProductBuyDTO productBuyDTO) throws RestClientException {
        String url = "http://localhost:8083/store/sell";
        try {
            ProductDTO productDTO = new ProductDTO(productBuyDTO.getProductId());
            SellDTO sellDTO = restClient.post()
                    .uri(url).contentType(MediaType.APPLICATION_JSON)
                    .body(productDTO)
                    .retrieve()
                    .body(SellDTO.class);
            return sellDTO;
        } catch (RestClientException e) {
            return new SellDTO("teste");
        }
    }
}
