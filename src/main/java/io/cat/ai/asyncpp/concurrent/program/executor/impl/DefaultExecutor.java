package io.cat.ai.asyncpp.concurrent.program.executor.impl;

import io.cat.ai.asyncpp.concurrent.program.executor.ExecutorComponent;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;

import lombok.extern.java.Log;

import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.nonNull;

@Log
public class DefaultExecutor implements BaseExecutor {

    private Executor executor;
    private ExecutorComponent component = ExecutorComponent.NOT_PROVIDED;

    DefaultExecutor(Executor executor) {
        if (nonNull(executor))
            component = ExecutorComponent.PROVIDED;
        else
            log.warning("You provided null instance of java.util.concurrent.Executor");

        this.executor = executor;
    }

    @Override
    public void execute(Runnable command) {
        switch (component) {
            case PROVIDED:
                executor.execute(command);
                break;
            case NOT_PROVIDED:
                throw new IllegalStateException("java.util.concurrent.Executor not provided!");
        }
    }

    @Override
    public void swallowFailure(Throwable cause) {
        log.warning(cause::getMessage);
    }

    @Override
    public void executeWithDelay(Runnable runnable, Duration duration) {
        DELAYED_EXECUTOR.schedule(runnable, duration.toMillis(), TimeUnit.MILLISECONDS);
    }

    public static BaseExecutor fromExecutor(Executor executor) {
        return new DefaultExecutor(executor);
    }
}
