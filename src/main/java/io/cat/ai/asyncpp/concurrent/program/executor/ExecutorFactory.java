package io.cat.ai.asyncpp.concurrent.program.executor;

import io.cat.ai.asyncpp.concurrent.program.executor.impl.DefaultExecutor;
import io.cat.ai.asyncpp.concurrent.program.executor.utils.ProgramExecutorUtils;

import lombok.*;

import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.Executor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutorFactory {

    private static final AbstractExecutorService defaultForkJoinPool = ProgramExecutorUtils.JavaConcurrentApi.ExecutorServices.defaultForkJoinPool();

    public static BaseExecutor newExecutor() {
        return newExecutor(defaultForkJoinPool);
    }

    public static BaseExecutor newExecutor(Executor executor) {
        return DefaultExecutor.fromExecutor(executor);
    }
}