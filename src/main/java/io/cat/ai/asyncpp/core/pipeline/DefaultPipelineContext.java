package io.cat.ai.asyncpp.core.pipeline;

import io.cat.ai.asyncpp.core.computation.AbstractBackgroundComputationEntity;

import java.time.Duration;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

public final class DefaultPipelineContext<T> extends AbstractPipelineContext<T> {

    DefaultPipelineContext(String name, AbstractBackgroundComputationEntity<T> entity) {
        super(name, entity);
    }

    @Override
    public AbstractPipelineContext<T> head() {
        return this;
    }

    @Override
    public AbstractPipelineContext<T> tail() {
        return next;
    }

    @Override
    public void run(T t) {
        try {
            entity.compute(t);
        } catch (Exception ignored) {
        }
    }

    @Override
    public void runAsync(CompletableFuture<T> future) {
        entity.computeAsync(future);
    }

    @Override
    public void runWithDelay(T t, Duration duration) {
        entity.computeWithDelay(t, duration);
    }

    @Override
    public void runBatch(Collection<T> all) {
        try {
            entity.startSerialComputing(all);
        } catch (Exception ignored) {
        }
    }

    @Override
    public String toString() {
        return "DefaultPipelineContext { name=" + name + "}";
    }
}