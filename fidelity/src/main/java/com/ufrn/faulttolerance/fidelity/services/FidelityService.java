package com.ufrn.faulttolerance.fidelity.services;

import com.ufrn.faulttolerance.fidelity.models.dto.BonusRequestDTO;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service
public class FidelityService {

    private final Map<String, Integer> userBonuses = new HashMap<>();

    public boolean registerBonus(BonusRequestDTO bonusRequestDTO) {
        userBonuses.put(bonusRequestDTO.getUser(), bonusRequestDTO.getBonus());
        return true;
    }
}
