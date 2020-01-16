package io.cat.ai.asyncpp.concurrent.computation.primitive;

public interface Unit<T> {
    void compute(T t) throws Exception;
}