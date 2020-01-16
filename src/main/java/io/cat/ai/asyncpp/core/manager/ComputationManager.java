package io.cat.ai.asyncpp.core.manager;

import io.cat.ai.asyncpp.concurrent.computation.BaseComputation;
import io.cat.ai.asyncpp.core.manager.component.ComputationState;
import io.cat.ai.asyncpp.core.manager.component.ComputationType;

import java.time.Duration;
import java.util.Collection;

public interface ComputationManager<T> {

    <C> void mark(BaseComputation<C> computation, ComputationType type, ComputationState state, Object ref);

    <C> void markDelayed(BaseComputation<C> computation, ComputationState state, Duration duration, Object ref);

    <I, O> void markIntersect(BaseComputation<I> in, BaseComputation<O> out, ComputationState state, Object ref);

    Collection<T> getAll(ComputationState state);

    Collection<T> getAll(ComputationType type);
}