package com.ufrn.faulttolerance.ecommerce.services;

import com.ufrn.faulttolerance.ecommerce.model.Product;
import com.ufrn.faulttolerance.ecommerce.model.dto.BonusRequestDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.SellDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.EntityResponse;

@Service
public class EcommerceService {

    private static double lastValidExchangeRate = 7.0;

    private RestClient restClient;

    public EcommerceService() {
        this.restClient = RestClient.create();
    }

    public Product buyProduct(ProductBuyDTO productBuyDTO) throws RestClientException {
        Product product = getProduct(productBuyDTO.getProductId());
        double rate = getExchangeRate();
        SellDTO sellDTO = sellProduct(productBuyDTO);
        boolean result = getBonus(productBuyDTO.getUserId(), product.getValue());
        return product;

    }

    @Cacheable(value = "exchangeRates", key = "'lastValidRate'")
    public Double getExchangeRate() {
        String urlPrimary = "http://exchange1:8080/exchange";
        String urlSecondary = "http://exchange2:8080/exchange";

        try {
            return restClient.get()
                    .uri(urlPrimary)
                    .retrieve()
                    .body(Double.class);
        } catch (RestClientException primaryFailure) {
            try {
                return restClient.get()
                        .uri(urlSecondary)
                        .retrieve()
                        .body(Double.class);
            } catch (RestClientException secondaryFailure) {
                throw new RuntimeException("Both replicas failed, using last cached rate.");
            }
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

    public boolean getBonus(String user, int bonus) throws RestClientException {
        String url = "http://localhost:8082/fidelity/bonus";
        try {
            BonusRequestDTO bonusRequestDTO = new BonusRequestDTO(user, bonus);
            ResponseEntity<Void> response = restClient.post()
                    .uri(url).contentType(MediaType.APPLICATION_JSON)
                    .body(bonusRequestDTO)
                    .retrieve()
                    .toBodilessEntity();
            return true;
        } catch (RestClientException e) {
            return false;
        }
    }
}
