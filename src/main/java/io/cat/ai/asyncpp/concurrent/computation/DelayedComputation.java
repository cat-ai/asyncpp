package io.cat.ai.asyncpp.concurrent.computation;

public interface DelayedComputation {
    void startComputingWithDelay(long millis) throws Exception;
}