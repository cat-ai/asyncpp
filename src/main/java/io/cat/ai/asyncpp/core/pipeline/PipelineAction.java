package io.cat.ai.asyncpp.core.pipeline;

import java.util.function.Consumer;

@FunctionalInterface
public interface PipelineAction<T> extends Consumer<T> { }