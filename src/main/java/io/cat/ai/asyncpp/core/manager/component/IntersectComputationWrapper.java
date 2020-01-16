package io.cat.ai.asyncpp.core.manager.component;

import io.cat.ai.asyncpp.concurrent.computation.BaseComputation;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@EqualsAndHashCode(callSuper = true)
public class IntersectComputationWrapper extends ComputationWrapper {

    private BaseComputation<?> entity;

    @Getter @Setter
    private BaseComputation<?> out;

    private ComputationType type;
    private ComputationState state;
    private Object ref;

    public IntersectComputationWrapper(BaseComputation<?> entity,
                                       BaseComputation<?> out,
                                       ComputationType type,
                                       ComputationState state,
                                       Object ref) {
        this(entity, type, state, ref);
        this.out = out;
    }

    public IntersectComputationWrapper(BaseComputation<?> entity, ComputationType type, ComputationState state, Object ref) {
        super(entity, type, state, ref);
    }
}