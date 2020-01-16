package io.cat.ai.asyncpp.concurrent.program;

import java.time.Duration;

public interface ProgramLogicDelayedExecutor {
    void executeWithDelay(Runnable runnable, Duration duration);
}