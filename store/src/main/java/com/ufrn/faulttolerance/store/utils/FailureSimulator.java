package com.ufrn.faulttolerance.store.utils;

import java.util.Random;

public class FailureSimulator {

    private final double failureProbability;
    private final int failureDurationMs;
    private boolean isFailureActive = false;
    private long failureEndTime = 0L;
    private final Random random = new Random();

    public FailureSimulator(double failureProbability, int failureDurationMs) {
        this.failureProbability = failureProbability;
        this.failureDurationMs = failureDurationMs;
    }

    public boolean isFailureActive() {
        if (isFailureActive && System.currentTimeMillis() > failureEndTime) {
            isFailureActive = false;
        }
        return isFailureActive;
    }

    public boolean shouldTriggerFailure() {
        return Math.random() < failureProbability;
    }

    public void activateFailure() {
        isFailureActive = true;
        failureEndTime = System.currentTimeMillis() + failureDurationMs;
    }
}
