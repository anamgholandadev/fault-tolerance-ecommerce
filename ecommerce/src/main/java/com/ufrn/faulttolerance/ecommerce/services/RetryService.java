package com.ufrn.faulttolerance.ecommerce.services;

import com.ufrn.faulttolerance.ecommerce.model.dto.BonusRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.time.Duration;

@Service
public class RetryService {

    @Autowired
    private RequestQueueService queueService;

    private final RestClient restClient;

    public RetryService() {
        RestTemplateBuilder builder = new RestTemplateBuilder();
        this.restClient = RestClient.create(builder
                .connectTimeout(Duration.ofSeconds(1))
                .readTimeout(Duration.ofSeconds(1))
                .build());
    }

    @Scheduled(fixedRate = 31000)
    public void reprocessQueue() {
        while (!queueService.isEmpty()) {
            BonusRequestDTO bonusRequestDTO = queueService.peek();
            String url = "http://fidelity:8080/fidelity/bonus";
            try {
                ResponseEntity<Void> response = restClient.post()
                        .uri(url).contentType(MediaType.APPLICATION_JSON)
                        .body(bonusRequestDTO)
                        .retrieve()
                        .toBodilessEntity();
                if (response.getStatusCode().is2xxSuccessful()) {
                    queueService.dequeue();
                }
            } catch (Exception e) {
                return;
            }
        }
    }
}