package io.cat.ai.asyncpp.concurrent.program;

import io.cat.ai.asyncpp.concurrent.program.executor.*;

import lombok.*;

import java.util.concurrent.Executor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ProgramLogicExecution {

    public final static BaseExecutor BASE_EXECUTOR = baseExecutor();

    private static BaseExecutor baseExecutor() {
        return ExecutorFactory.newExecutor();
    }

    public static BaseExecutor executorFrom(Executor executor) {
        return ExecutorFactory.newExecutor(executor);
    }
}