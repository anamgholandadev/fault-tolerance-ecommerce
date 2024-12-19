package com.ufrn.faulttolerance.fidelity.controllers;

import com.ufrn.faulttolerance.fidelity.models.dto.BonusRequestDTO;
import com.ufrn.faulttolerance.fidelity.services.FidelityService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FidelityController {

    private final FidelityService fidelityService;

    public FidelityController(FidelityService fidelityService) {
        this.fidelityService = fidelityService;
    }

    @PostMapping("/bonus")
    public ResponseEntity<String> registerBonus(@RequestBody BonusRequestDTO bonusRequestDTO) {
        boolean success = fidelityService.registerBonus(bonusRequestDTO);
        if (success) {
            return ResponseEntity.ok("Bonus registered successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to register bonus.");
        }
    }

}
