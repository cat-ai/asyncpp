package io.cat.ai.asyncpp.concurrent.program.executor;

import io.cat.ai.asyncpp.concurrent.program.ProgramLogicDelayedExecutor;
import io.cat.ai.asyncpp.concurrent.program.ProgramLogicExecutor;
import io.cat.ai.asyncpp.concurrent.program.ProgramLogicFailure;
import io.cat.ai.asyncpp.concurrent.program.executor.utils.ProgramExecutorUtils;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;

public interface BaseExecutor extends Executor, ProgramLogicExecutor, ProgramLogicDelayedExecutor, ProgramLogicFailure {
    ScheduledExecutorService DELAYED_EXECUTOR = ProgramExecutorUtils.JavaConcurrentApi.ExecutorServices.delayed();
}