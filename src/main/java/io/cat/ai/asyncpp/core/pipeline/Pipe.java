package io.cat.ai.asyncpp.core.pipeline;

public interface Pipe {
    <N> AbstractPipeline<N> pipe(PipelineAction<N> step);
}