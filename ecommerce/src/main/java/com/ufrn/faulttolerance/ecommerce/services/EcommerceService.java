package com.ufrn.faulttolerance.ecommerce.services;

import com.ufrn.faulttolerance.ecommerce.model.Product;
import com.ufrn.faulttolerance.ecommerce.model.dto.BonusRequestDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductBuyDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.ProductDTO;
import com.ufrn.faulttolerance.ecommerce.model.dto.SellDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.core.functions.CheckedSupplier;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import io.github.resilience4j.retry.RetryRegistry;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.time.Duration;
import java.util.function.Supplier;

@Service
public class EcommerceService {

    private static double lastValidExchangeRate = 7.0;

    private RestClient restClient;
    private CircuitBreaker circuitBreaker;
    private RequestQueueService queueService;

    public EcommerceService(RequestQueueService queueService) {
        this.restClient = RestClient.create();
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(100)
                .minimumNumberOfCalls(1)
                .waitDurationInOpenState(Duration.ofSeconds(30))
                .build();
        CircuitBreakerRegistry registry = CircuitBreakerRegistry.of(config);
        this.circuitBreaker = registry.circuitBreaker("circuitBreaker-ecommerce-store");
        this.queueService = queueService;
    }

    public void buyProduct(ProductBuyDTO productBuyDTO) throws Exception {
        setTimeout(productBuyDTO.isFt());
        Product product = shouldRetryProduct(productBuyDTO);
        shouldUseToleranceExchange(productBuyDTO.isFt());
        shouldUseCB(productBuyDTO);
        getBonus(productBuyDTO.getUserId(), 10, productBuyDTO.isFt());
    }

    private Product shouldRetryProduct(ProductBuyDTO productBuyDTO) {
        if (productBuyDTO.isFt()) {
            RetryConfig config = RetryConfig.custom()
                    .maxAttempts(3).retryOnException(e -> e instanceof RestClientException)
                    .build();
            RetryRegistry registry = RetryRegistry.of(config);
            Retry retry = registry.retry("omission-fault-store");
            CheckedSupplier<Product> decoratedSupplier = Retry.decorateCheckedSupplier(retry,
                    () -> getProduct(productBuyDTO.getProductId()));
            try {
                return decoratedSupplier.get();
            } catch (Throwable e) {
                throw new RuntimeException("Erro após tentativas de retry", e);
            }
        } else {
            return getProduct(productBuyDTO.getProductId());
        }
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
                        throw e;
                    }
                }).unchecked();
        try {
            return decoratedSupplier.get();
        } catch (Exception e) {
            throw new RuntimeException("Erro após tentativas de circuit breaker", e);
        }
    }

    public Double getExchangeRateWithTolerance() {
        String urlPrimary = "http://exchange1:8080/exchange";
        String urlSecondary = "http://exchange2:8080/exchange";

        try {
            Double rate = getExchangeRate(urlPrimary);
            lastValidExchangeRate = rate;
            return rate;
        } catch (RestClientException primaryFailure) {
            try {
                Double rate = getExchangeRate(urlSecondary);
                lastValidExchangeRate = rate;
                return rate;
            } catch (RestClientException secondaryFailure) {
                return lastValidExchangeRate;

            }
        }
    }

    private Double shouldUseToleranceExchange(boolean ft) {
        if (ft) {
            return getExchangeRateWithTolerance();
        } else {
            return getExchangeRate("http://exchange1:8080/exchange");
        }
    }

    private Double getExchangeRate(String url) {
        return restClient.get()
                .uri(url)
                .retrieve()
                .body(Double.class);
    }

    public Product getProduct(String productId) throws RestClientException {
        String url = "http://store:8080/store/product/" + productId;
        Product product = restClient.get()
                .uri(url)
                .retrieve()
                .body(Product.class);
        return product;
    }

    public SellDTO sellProduct(ProductBuyDTO productBuyDTO) throws RestClientException {
        String url = "http://store:8080/store/sell";
        ProductDTO productDTO = new ProductDTO(productBuyDTO.getProductId());
        SellDTO sellDTO = restClient.post()
                .uri(url).contentType(MediaType.APPLICATION_JSON)
                .body(productDTO)
                .retrieve()
                .body(SellDTO.class);
        return sellDTO;
    }

    public void getBonus(String user, int bonus, boolean ft) {
        try {
            BonusRequestDTO bonusRequestDTO = new BonusRequestDTO(user, bonus);
            String url = "http://fidelity:8080/fidelity/bonus";

            ResponseEntity<Void> response = restClient.post()
                    .uri(url).contentType(MediaType.APPLICATION_JSON)
                    .body(bonusRequestDTO)
                    .retrieve()
                    .toBodilessEntity();

        } catch (Exception e) {
            if (ft) {
                queueService.enqueue(new BonusRequestDTO(user, bonus));
            } else {
                queueService.clear();
            }
            e.printStackTrace();
        }
    }
}
