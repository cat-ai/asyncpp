package io.cat.ai.asyncpp.concurrent.computation;

import java.util.Collection;

public interface SerialComputation<T> {
    void startSerialComputing(Collection<T> t) throws Exception;
}