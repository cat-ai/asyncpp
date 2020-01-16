package io.cat.ai.asyncpp.core.manager.component;

import io.cat.ai.asyncpp.concurrent.computation.BaseComputation;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Duration;

@ToString
public class DelayedComputationWrapper extends ComputationWrapper {

    private static final ComputationType DELAYED_TYPE_CONST = ComputationType.DELAYED_COMPUTING;

    private BaseComputation<?> entity;
    private ComputationType type;
    private ComputationState state;

    @Getter @Setter
    private Duration duration;
    private Object ref;

    public DelayedComputationWrapper(BaseComputation<?> entity, ComputationState state, Duration duration, Object ref) {
        super(entity, DELAYED_TYPE_CONST, state, ref);
        this.entity = entity;
        this.type = DELAYED_TYPE_CONST;
        this.state = state;
        this.duration = duration;
        this.ref = ref;
    }
}