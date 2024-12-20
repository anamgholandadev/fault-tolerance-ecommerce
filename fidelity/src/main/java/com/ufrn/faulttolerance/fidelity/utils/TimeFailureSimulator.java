package com.ufrn.faulttolerance.fidelity.utils;

public class TimeFailureSimulator {

    private final double failureProbability;
    private final int failureDurationMs;
    private final int delayDurationMs;
    private boolean isFailureActive = false;
    private long failureEndTime = 0L;

    public TimeFailureSimulator(double failureProbability, int failureDurationMs, int delayDurationMs) {
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
        return Math.random() < failureProbability;
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
