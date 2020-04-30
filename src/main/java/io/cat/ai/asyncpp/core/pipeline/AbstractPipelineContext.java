package io.cat.ai.asyncpp.core.pipeline;

import io.cat.ai.asyncpp.core.computation.AbstractBackgroundComputationEntity;

import lombok.AllArgsConstructor;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
abstract class AbstractPipelineContext<T> {

    final String name;
    final AbstractBackgroundComputationEntity<T> entity;

    volatile AbstractPipelineContext<T> next;
    volatile AbstractPipelineContext<T> prev;

    public AbstractPipelineContext(String name, AbstractBackgroundComputationEntity<T> entity) {
        this.name = name;
        this.entity = entity;
    }

    public abstract AbstractPipelineContext<T> next();
    public abstract AbstractPipelineContext<T> prev();

    public abstract void run(T t);
    public abstract void runAsync(CompletableFuture<T> f);
    public abstract void runWithDelay(T t, Duration duration);
    public abstract void runBatch(Collection<T> all);
}