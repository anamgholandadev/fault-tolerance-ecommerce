package com.ufrn.faulttolerance.ecommerce.services;

import com.ufrn.faulttolerance.ecommerce.model.Product;
import com.ufrn.faulttolerance.ecommerce.model.dto.BonusRequestDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.SellDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.http.MediaType;


import java.time.Duration;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
public class EcommerceService {

    private static double lastValidExchangeRate = 7.0;

    private RestClient restClient;
    private CircuitBreaker circuitBreaker;

    public EcommerceService() {
        this.restClient = RestClient.create();
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(100) // 100% das falhas
                .minimumNumberOfCalls(1) // Considera uma falha após uma chamada
                .waitDurationInOpenState(Duration.ofSeconds(30)) //Tempo de espera em estado aberto
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        this.circuitBreaker = registry.circuitBreaker("circuitBreaker-ecommerce-store");
    }

    public SellDTO buyProduct(ProductBuyDTO productBuyDTO) throws RestClientException {
        setTimeout(productBuyDTO.isFt());
        Product product = getProduct(productBuyDTO.getProductId());
        double rate = getExchangeRate();
        SellDTO sellDTO = shouldUseCB(productBuyDTO);
        boolean result = getBonus(productBuyDTO.getUserId(), product.getValue());
        return sellDTO;
    }

    private void setTimeout(boolean ft) {
        if (ft) {
            RestTemplateBuilder builder = new RestTemplateBuilder();
            this.restClient = RestClient.create(builder
                    .connectTimeout(Duration.ofSeconds(1))
                    .readTimeout(Duration.ofSeconds(1))
                    .build());
        }
    }

    private SellDTO shouldUseCB(ProductBuyDTO productBuyDTO) {
        if (productBuyDTO.isFt()) {
            return initCircuitBreak(productBuyDTO);
        } else {
            return sellProduct(productBuyDTO);
        }
    }

    private SellDTO initCircuitBreak(ProductBuyDTO productBuyDTO) {
        Supplier<SellDTO> decoratedSupplier = CircuitBreaker
                .decorateCheckedSupplier(circuitBreaker, () -> {
                    try {
                        var sellDTO = sellProduct(productBuyDTO);
                        return sellDTO;
                    } catch (RestClientException e) {
                        throw e;  // Propaga a exceção para o Circuit Breaker monitorar
                    }
                }).unchecked();
        try {
            return decoratedSupplier.get();
        } catch (Exception e) {// Caso o Circuit Breaker esteja aberto ou o tempo limite seja atingido
            return new SellDTO("teste");
        }
    }

    public Double getExchangeRate() {
        String urlPrimary = "http://exchange1:8080/exchange";
        String urlSecondary = "http://exchange2:8080/exchange";

        try {
            Double rate = restClient.get()
                    .uri(urlPrimary)
                    .retrieve()
                    .body(Double.class);
            lastValidExchangeRate = rate;
            return rate;
        } catch (RestClientException primaryFailure) {
            try {
                Double rate = restClient.get()
                        .uri(urlSecondary)
                        .retrieve()
                        .body(Double.class);
                lastValidExchangeRate = rate;
                return rate;
            } catch (RestClientException secondaryFailure) {
                return lastValidExchangeRate;

            }
        }
    }

    public Product getProduct(String productId) throws RestClientException {
        String url = "http://localhost:8083/store/product/" + productId;
        Product product = restClient.get()
                .uri(url)
                .retrieve()
                .body(Product.class);
        return product;
    }

    public SellDTO sellProduct(ProductBuyDTO productBuyDTO) throws RestClientException {
        String url = "http://localhost:8083/store/sell";
        ProductDTO productDTO = new ProductDTO(productBuyDTO.getProductId());
        SellDTO sellDTO = restClient.post()
                .uri(url).contentType(MediaType.APPLICATION_JSON)
                .body(productDTO)
                .retrieve()
                .body(SellDTO.class);
        return sellDTO;
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
