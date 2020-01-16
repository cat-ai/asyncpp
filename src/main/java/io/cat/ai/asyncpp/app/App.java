package io.cat.ai.asyncpp.app;

import io.cat.ai.asyncpp.app.internal.Asyncpp;
import io.cat.ai.asyncpp.app.internal.impl.AsyncppFactory;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;
import io.cat.ai.asyncpp.core.pipeline.AbstractPipeline;

public class App {

    public static class Dsl {

        public static <T> Asyncpp<T> asyncpp() {
            return AsyncppFactory.getInstance();
        }

        public static <T> Asyncpp<T> asyncpp(BaseExecutor baseExecutor) {
            return AsyncppFactory.fromExecutor(baseExecutor);
        }

        public static <T> Asyncpp<T> asyncpp(AbstractPipeline<T> pipeline) {
            return AsyncppFactory.fromPipeline(pipeline);
        }

        public static <T> Asyncpp<T> asyncpp(BaseExecutor baseExecutor, AbstractPipeline<T> pipeline) {
            return AsyncppFactory.fromExecutorAndPipeline(baseExecutor, pipeline);
        }
    }
}