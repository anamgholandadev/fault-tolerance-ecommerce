package com.ufrn.faulttolerance.fidelity.services;

import com.ufrn.faulttolerance.fidelity.models.dto.BonusRequestDTO;
import com.ufrn.faulttolerance.fidelity.utils.TimeFailureSimulator;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class FidelityService {

    private final Map<String, Integer> userBonuses = new HashMap<>();
    private final TimeFailureSimulator timeFailureSimulator;

    public FidelityService() {
        this.timeFailureSimulator = new TimeFailureSimulator(0.1, 30000, 2000);
    }

    public boolean registerBonus(BonusRequestDTO bonusRequestDTO) {
        if (timeFailureSimulator.shouldTriggerFailure()) {
            timeFailureSimulator.activateFailure();
        }
        timeFailureSimulator.simulateFailureDelay();

        userBonuses.put(bonusRequestDTO.getUser(), bonusRequestDTO.getBonus());
        return true;
    }
}
