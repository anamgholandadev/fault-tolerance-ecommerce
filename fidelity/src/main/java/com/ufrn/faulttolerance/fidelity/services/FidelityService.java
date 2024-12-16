package com.ufrn.faulttolerance.fidelity.services;

import com.ufrn.faulttolerance.fidelity.models.dto.BonusRequestDTO;
import com.ufrn.faulttolerance.fidelity.utils.FailureSimulator;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class FidelityService {

    private final Map<String, Integer> userBonuses = new HashMap<>();
    private final FailureSimulator failureSimulator;

    public FidelityService() {
        this.failureSimulator = new FailureSimulator(0.1, 30000, 2000);
    }

    public boolean registerBonus(BonusRequestDTO bonusRequestDTO) {
        if (failureSimulator.shouldTriggerFailure()) {
            failureSimulator.activateFailure();
        }
        failureSimulator.simulateFailureDelay();

        userBonuses.put(bonusRequestDTO.getUser(), bonusRequestDTO.getBonus());
        return true;
    }
}
