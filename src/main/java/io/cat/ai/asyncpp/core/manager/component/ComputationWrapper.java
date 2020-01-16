package io.cat.ai.asyncpp.core.manager.component;

import io.cat.ai.asyncpp.concurrent.computation.BaseComputation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ComputationWrapper {

    private BaseComputation<?> entity;
    private ComputationType type;
    private ComputationState state;
    private Object ref;
}