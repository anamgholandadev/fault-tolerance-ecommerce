package com.ufrn.faulttolerance.store.utils;

import java.util.Random;

public class FailureSimulator {

    private final double failureProbability;
    private final int failureDurationMs;
    private final int delayDurationMs;
    private boolean isFailureActive = false;
    private long failureEndTime = 0L;
    private final Random random = new Random();

    public FailureSimulator(double failureProbability, int failureDurationMs, int delayDurationMs) {
        this.failureProbability = failureProbability;
        this.failureDurationMs = failureDurationMs;
        this.delayDurationMs = delayDurationMs;
    }

    public boolean isFailureActive() {
        if (isFailureActive && System.currentTimeMillis() > failureEndTime) {
            isFailureActive = false;
        }
        return isFailureActive;
    }

    public boolean shouldTriggerFailure() {
        if(failureProbability == 0.1) {
            return random.nextInt(10) == 0;
        }else{
            return  random.nextInt(5) == 0;
        }
    }

    public void activateFailure() {
        isFailureActive = true;
        failureEndTime = System.currentTimeMillis() + failureDurationMs;
    }

    public void simulateFailureDelay() {
        if (isFailureActive()) {
            try {
                Thread.sleep(delayDurationMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
