package io.cat.ai.asyncpp.concurrent.program;

public interface ProgramLogicFailure {
    void swallowFailure(Throwable cause);
}