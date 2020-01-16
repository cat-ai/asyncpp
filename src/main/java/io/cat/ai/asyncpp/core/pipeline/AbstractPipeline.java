package io.cat.ai.asyncpp.core.pipeline;

import io.cat.ai.asyncpp.core.computation.AbstractBackgroundComputationEntity;
import io.cat.ai.asyncpp.core.manager.ComputationManager;
import io.cat.ai.asyncpp.core.manager.component.ComputationWrapper;

import java.time.Duration;
import java.util.Collection;
import java.util.Map;

public abstract class AbstractPipeline<T> implements Iterable<Map.Entry<String, AbstractBackgroundComputationEntity<T>>> {

    AbstractPipelineContext<T> head;

    public abstract void run(T t);

    public abstract void runOn(String name, T t);

    public abstract void runWithDelay(T t, Duration duration);

    public abstract void runWithDelayOn(String name, T t, Duration duration);

    public abstract void runBatch(Collection<T> all);

    public abstract void runBatchOn(String name, Collection<T> all);

    public abstract void runAsync(T t);

    public abstract void runAsyncOn(String name, T t);

    public abstract AbstractPipeline<T> addFirst(String name, AbstractBackgroundComputationEntity<T> step);

    public abstract AbstractPipeline<T> addLast(String name, AbstractBackgroundComputationEntity<T> step);

    public abstract AbstractPipeline<T> addBefore(String prevName, String newName, AbstractBackgroundComputationEntity<T> newEntity);

    public abstract AbstractPipeline<T> addAfter(String prevName, String newName, AbstractBackgroundComputationEntity<T> newEntity);

    public abstract AbstractPipeline<T> removeFirst();

    public abstract AbstractPipeline<T> removeLast();

    public abstract AbstractPipeline<T> remove(String name);

    public abstract AbstractPipeline<T> printWholePipeline();

    public abstract ComputationManager<ComputationWrapper> computationManager();

    public abstract int length();
}