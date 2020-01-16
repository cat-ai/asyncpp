package io.cat.ai.asyncpp.app.internal;

import java.time.Duration;
import java.util.Collection;

public interface PipelineProcessor<T> {

    PipelineProcessor<T> process(T t);

    PipelineProcessor<T> processOn(String name, T t);

    PipelineProcessor<T> processAsync(T t);

    PipelineProcessor<T> processAsyncOn(String name, T t);

    PipelineProcessor<T> processWithDelay(T t, Duration duration);

    PipelineProcessor<T> processWithDelayOn(String name, T t, Duration duration);

    PipelineProcessor<T> processBatch(Collection<T> all);

    PipelineProcessor<T> processBatchOn(String name, Collection<T> all);
}