package io.cat.ai.asyncpp.core.computation;

import io.cat.ai.asyncpp.core.manager.component.ComputationState;
import io.cat.ai.asyncpp.core.manager.component.ComputationType;

import lombok.val;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Collections;

public abstract class AbstractBatchComputationEntity<T> extends AbstractDelayedComputationEntity<T> {

    public AbstractBatchComputationEntity() {
        super(new ArrayDeque<>());
    }

    @Override
    public void startComputing(T t) throws Exception {
        this.startSerialComputing(Collections.singleton(t));
    }

    @Override
    public void compute(T t) throws Exception {
        this.startComputing(t);
    }

    @Override
    public void startSerialComputing(Collection<T> elems) throws Exception {
        try {
            for (val elem: elems)
                computationManager.mark(this, ComputationType.BATCH_COMPUTING, ComputationState.PREPARED, elem);

            computeAll(elems);

            computationManager.mark(this, ComputationType.BATCH_COMPUTING, ComputationState.COMPLETED, elems);
        } catch (Exception cause) {
            computationManager.mark(this, ComputationType.BATCH_COMPUTING, ComputationState.FAILED, elems);
            executor.swallowFailure(cause);
        }
    }

    public abstract void computeAll(Collection<T> all) throws Exception;
}