package io.cat.ai.asyncpp.core.computation;

import io.cat.ai.asyncpp.concurrent.computation.DelayedComputation;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;
import io.cat.ai.asyncpp.core.manager.ComputationManager;
import io.cat.ai.asyncpp.core.manager.component.ComputationState;
import io.cat.ai.asyncpp.core.manager.component.ComputationType;
import io.cat.ai.asyncpp.core.manager.component.ComputationWrapper;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Queue;

public abstract class AbstractDelayedComputationEntity<T> extends AbstractBackgroundComputationEntity<T> implements DelayedComputation {

    private static final Object LOCK = new Object();

    protected boolean isDelayed;

    public AbstractDelayedComputationEntity(Queue<T> refQueue) {
        super(refQueue);
    }

    private void delayedComputation(Collection<T> all, Duration duration) {
        all.forEach(elem -> computeWithDelay(elem, duration));
    }

    @Override
    public void computeWithDelay(T ref, Duration duration) {
        computationManager.markDelayed(this, ComputationState.PREPARED, duration, ref);

        executor.executeWithDelay(
                () -> {
                    try {
                        computationManager.markDelayed(this, ComputationState.STARTED, duration, ref);
                        compute(ref);
                    } catch (Exception cause) {
                        computationManager.markDelayed(this, ComputationState.FAILED, duration, ref);
                        executor.swallowFailure(cause);
                    }
                }, duration);

        computationManager.markDelayed(this, ComputationState.COMPLETED, duration, ref);
    }

    @Override
    public BaseExecutor executor() {
        return this.executor;
    }

    @Override
    public ComputationManager<ComputationWrapper> computationManager() {
        return super.computationManager;
    }

    @Override
    public void startComputingWithDelay(long millis) throws Exception {
        Collection<T> all;

        synchronized(LOCK) {
            all = new ArrayList<>(refQueue);

            if (millis > 0L)
                delayedComputation(all, Duration.ofMillis(millis));

            startSerialComputing(all);

            if (!all.isEmpty())
                executor.execute(() -> {
                    try {
                        startComputingWithDelay(0L);
                    } catch (Exception cause) {
                        executor.swallowFailure(cause);
                    }
                });
            else
                isDelayed = false;
        }
    }

    @Override
    public abstract void startComputing(T t) throws Exception;
}
