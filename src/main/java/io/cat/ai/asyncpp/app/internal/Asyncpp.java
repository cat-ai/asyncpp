package io.cat.ai.asyncpp.app.internal;

import io.cat.ai.asyncpp.core.computation.AbstractBackgroundComputationEntity;
import io.cat.ai.asyncpp.core.pipeline.AbstractPipeline;

import java.time.Duration;
import java.util.Collection;
import java.util.function.Supplier;

public interface Asyncpp<T> extends ProcessingPipeline<T>, PipelineProcessor<T>, ProcessingPipelineEntities<T> {

    Asyncpp<T> createPipeline();

    Asyncpp<T> printWholeProcessingPipeline();

    @Override
    Asyncpp<T> withPipeline(AbstractPipeline<T> pipeline);

    @Override
    Asyncpp<T> withEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    @Override
    Asyncpp<T> withFirstEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    @Override
    Asyncpp<T> withLastEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    @Override
    Asyncpp<T> withEntityBefore(String entityNameBefore, String entityNameAfter, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    @Override
    Asyncpp<T> withEntityAfter(String entityName, String newEntityName, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    @Override
    Asyncpp<T> removeEntity(String entityName);

    @Override
    Asyncpp<T> removeLastEntity();

    @Override
    Asyncpp<T> removeFirstEntity();

    @Override
    Asyncpp<T> process(T t);

    @Override
    Asyncpp<T> processOn(String name, T t);

    @Override
    Asyncpp<T> processAsync(T t);

    @Override
    Asyncpp<T> processAsyncOn(String name, T t);

    @Override
    Asyncpp<T> processWithDelay(T t, Duration duration);

    @Override
    Asyncpp<T> processWithDelayOn(String name, T t, Duration duration);

    @Override
    Asyncpp<T> processBatch(Collection<T> all);

    @Override
    Asyncpp<T> processBatchOn(String name, Collection<T> all);
}