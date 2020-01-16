package io.cat.ai.asyncpp.core.computation;

import io.cat.ai.asyncpp.concurrent.computation.BaseComputation;
import io.cat.ai.asyncpp.concurrent.computation.SerialComputation;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;
import io.cat.ai.asyncpp.core.manager.ComputationManager;
import io.cat.ai.asyncpp.core.manager.ComputationManagerAgent;
import io.cat.ai.asyncpp.core.manager.component.ComputationState;
import io.cat.ai.asyncpp.core.manager.component.ComputationType;
import io.cat.ai.asyncpp.core.manager.component.ComputationWrapper;

import lombok.EqualsAndHashCode;
import lombok.val;

import java.time.Duration;
import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.nonNull;

@EqualsAndHashCode(callSuper = false)
public abstract class AbstractBackgroundComputationEntity<T> extends BaseComputation<T> implements SerialComputation<T> {

    protected ComputationManager<ComputationWrapper> computationManager;

    public AbstractBackgroundComputationEntity(Queue<T> refQueue) {
        super(refQueue);
        this.computationManager = ComputationManagerAgent.getInstanceFromExecutor(executor);
    }

    @Override
    public void setExecutor(BaseExecutor anExecutor) {
        executor = anExecutor;
    }

    public abstract ComputationManager<ComputationWrapper> computationManager();

    public abstract void computeWithDelay(T ref, Duration duration);

    @Override
    public void startSerialComputing(Collection<T> elems) throws Exception {
        for (val elem: elems) {
            try {
                computationManager.mark(this, ComputationType.COMPUTING, ComputationState.STARTED, elem);
                startComputing(elem);
                refQueue.poll();
                computationManager.mark(this, ComputationType.COMPUTING, ComputationState.COMPLETED, elem);
            } catch (Exception cause) {
                computationManager.mark(this, ComputationType.COMPUTING, ComputationState.FAILED, elem);
                executor.swallowFailure(cause);
            }
        }
    }

    @Override
    public void computeAsync(CompletableFuture<T> future) {
        future.whenCompleteAsync(
                (result, cause) -> {
                    if (nonNull(cause)) {
                        executor.swallowFailure(cause);
                    } else {
                        computationManager.mark(this, ComputationType.ASYNC_COMPUTING, ComputationState.PREPARED, result);
                        try {
                            computationManager.mark(this, ComputationType.ASYNC_COMPUTING, ComputationState.STARTED, result);
                            compute(result);
                            computationManager.mark(this, ComputationType.ASYNC_COMPUTING, ComputationState.COMPLETED, result);
                        } catch (Exception anotherCause) {
                            computationManager.mark(this, ComputationType.ASYNC_COMPUTING, ComputationState.FAILED, result);
                            executor.swallowFailure(anotherCause);
                        }
                    }
                }, executor
        );
    }
}
