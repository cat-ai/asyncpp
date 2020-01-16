package io.cat.ai.asyncpp.app.internal;

import io.cat.ai.asyncpp.core.computation.AbstractBackgroundComputationEntity;

import java.util.function.Supplier;

public interface ProcessingPipelineEntities<T> {

    ProcessingPipelineEntities<T> withEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    ProcessingPipelineEntities<T> withFirstEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    ProcessingPipelineEntities<T> withLastEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    ProcessingPipelineEntities<T> withEntityBefore(String entityNameBefore, String entityNameAfter, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    ProcessingPipelineEntities<T> withEntityAfter(String entityName, String newEntityName, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier);

    ProcessingPipelineEntities<T> removeEntity(String entityName);

    ProcessingPipelineEntities<T> removeLastEntity();

    ProcessingPipelineEntities<T> removeFirstEntity();
}