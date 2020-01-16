package io.cat.ai.asyncpp.concurrent.computation;

import io.cat.ai.asyncpp.concurrent.computation.primitive.AsyncUnit;
import io.cat.ai.asyncpp.concurrent.computation.primitive.Unit;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;

import java.util.Queue;
import java.util.concurrent.CompletableFuture;

@AllArgsConstructor
@EqualsAndHashCode
public abstract class BaseComputation<T> implements Unit<T>, AsyncUnit<T, CompletableFuture<T>> {

    protected BaseExecutor executor;
    protected Queue<T> refQueue;

    public BaseComputation(Queue<T> refQueue) {
        this.refQueue = refQueue;
    }

    public abstract void startComputing(T t) throws Exception;

    public abstract void setExecutor(BaseExecutor anExecutor);

    public abstract BaseExecutor executor();

    @Override
    public String toString() {
        return "<" + this.getClass().getSimpleName() + ">";
    }
}