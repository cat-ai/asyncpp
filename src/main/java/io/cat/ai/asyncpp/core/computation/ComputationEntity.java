package io.cat.ai.asyncpp.core.computation;

import java.util.ArrayDeque;

public abstract class ComputationEntity<T> extends AbstractDelayedComputationEntity<T> {

    private static final Object LOCK = new Object();

    public ComputationEntity() {
        super(new ArrayDeque<>());
    }

    @Override
    public void compute(T t) {
        synchronized(LOCK) {
            refQueue.add(t);

            if (!isDelayed) {
                isDelayed = true;
                executor.execute(() -> {
                    try {
                        startComputingWithDelay(0L);
                    } catch (Exception __) {
                        // handled in startComputingWithDelay() method
                    }
                });
            }
        }
    }

    @Override
    public abstract void startComputing(T t) throws Exception;
}