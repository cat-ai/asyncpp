package io.cat.ai.asyncpp.concurrent.computation.primitive;

import java.util.concurrent.Future;

public interface AsyncUnit<T, F extends Future<? extends T>> {
    void computeAsync(F future) throws Exception;
}