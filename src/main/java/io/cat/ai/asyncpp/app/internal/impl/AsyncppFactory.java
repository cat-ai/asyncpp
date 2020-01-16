package io.cat.ai.asyncpp.app.internal.impl;

import io.cat.ai.asyncpp.app.internal.Asyncpp;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;
import io.cat.ai.asyncpp.core.pipeline.AbstractPipeline;

import static io.cat.ai.asyncpp.concurrent.program.ProgramLogicExecution.*;

public final class AsyncppFactory {

    public static <T> Asyncpp<T> fromExecutor(BaseExecutor baseExecutor) {
        return AsyncppImpl.fromExecutor(baseExecutor);
    }

    public static <T> Asyncpp<T> getInstance() {
        return fromExecutor(BASE_EXECUTOR);
    }

    public static <T> Asyncpp<T> fromPipeline(AbstractPipeline<T> pipeline) {
        return AsyncppFactory.
                <T>getInstance()
                .withPipeline(pipeline);
    }

    public static <T> Asyncpp<T> fromExecutorAndPipeline(BaseExecutor baseExecutor, AbstractPipeline<T> pipeline) {
        return AsyncppFactory.
                <T>fromExecutor(baseExecutor)
                .withPipeline(pipeline);
    }
}
