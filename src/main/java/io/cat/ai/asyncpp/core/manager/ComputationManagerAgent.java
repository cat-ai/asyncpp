package io.cat.ai.asyncpp.core.manager;

import io.cat.ai.asyncpp.concurrent.computation.BaseComputation;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;
import io.cat.ai.asyncpp.core.manager.component.*;

import lombok.EqualsAndHashCode;
import lombok.var;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@EqualsAndHashCode
public final class ComputationManagerAgent implements ComputationManager<ComputationWrapper> {

    private static volatile ComputationManagerAgent INSTANCE;

    private final BaseExecutor baseExecutor;
    private final List<ComputationWrapper> wrappers;

    private ComputationManagerAgent(BaseExecutor baseExecutor) {
        this.baseExecutor = baseExecutor;
        this.wrappers = new ArrayList<>();
    }

    public static ComputationManagerAgent getInstanceFromExecutor(BaseExecutor executor) {
        var localInstance = INSTANCE;

        if (localInstance == null)
            synchronized (ComputationManagerAgent.class) {
                localInstance = INSTANCE;
                if (localInstance == null)
                    INSTANCE = localInstance = new ComputationManagerAgent(executor);
            }

        return localInstance;
    }

    @Override
    public <C> void mark(BaseComputation<C> computation, ComputationType type, ComputationState state, Object ref) {
        wrappers.add(new ComputationWrapper(computation, type, state, ref));
    }

    @Override
    public <C> void markDelayed(BaseComputation<C> computation, ComputationState state, Duration duration, Object ref) {
        wrappers.add(new DelayedComputationWrapper(computation, state, duration, ref));
    }

    @Override
    public <I, O> void markIntersect(BaseComputation<I> in, BaseComputation<O> out, ComputationState state, Object ref) {
        wrappers.add(new IntersectComputationWrapper(in, out, ComputationType.INTERSECT_COMPUTING, state, ref));
    }

    @Override
    public Collection<ComputationWrapper> getAll(ComputationState state) {
        return wrappers.stream()
                .filter(__ -> __.getState() == state)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ComputationWrapper> getAll(ComputationType type) {
        return wrappers.stream()
                .filter(__ -> __.getType() == type)
                .collect(Collectors.toList());
    }
}
