package io.cat.ai.asyncpp.app.internal;

import io.cat.ai.asyncpp.core.pipeline.AbstractPipeline;

public interface ProcessingPipeline<T> {

    AbstractPipeline<T> pipeline();

    ProcessingPipeline<T> withPipeline(AbstractPipeline<T> pipeline);
}