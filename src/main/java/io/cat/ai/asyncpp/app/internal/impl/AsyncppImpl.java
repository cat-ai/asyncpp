package io.cat.ai.asyncpp.app.internal.impl;

import io.cat.ai.asyncpp.app.internal.Asyncpp;
import io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor;
import io.cat.ai.asyncpp.core.computation.AbstractBackgroundComputationEntity;
import io.cat.ai.asyncpp.core.pipeline.*;

import lombok.extern.java.Log;
import lombok.val;

import java.time.Duration;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.util.Objects.*;

@Log
final class AsyncppImpl<T> implements Asyncpp<T> {

    private BaseExecutor baseExecutor;
    private boolean isExecutorProvided;

    private AbstractPipeline<T> pipeline;
    private boolean isPipelineCreated;

    private Map<String, AbstractBackgroundComputationEntity<T>> entities = new LinkedHashMap<>();

    private AsyncppImpl(BaseExecutor baseExecutor) {
        initExecutor(baseExecutor);
    }

    private void initExecutor(BaseExecutor executor) {
        if (!isExecutorProvided) {

            if (nonNull(executor)) {
                this.baseExecutor = executor;
                this.isExecutorProvided = true;
            } else {
                log.warning("Provided io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor instance is null");
                throw new NullPointerException("io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor");
            }
        }
    }

    private void setExecutorForEntities() {
        this.entities
                .values()
                .stream()
                .filter(__ -> isNull(__.executor()))
                .forEach(__ -> __.setExecutor(baseExecutor));
    }

    private void setExecutorForEntity(String name) {
        this.entities
                .entrySet()
                .stream()
                .filter(__ -> __.getKey().equals(name))
                .map(Map.Entry::getValue)
                .findFirst()
                .ifPresent(__ -> __.setExecutor(baseExecutor));
    }

    private void checkBefore(final String method) {
        if (!isExecutorProvided || isNull(baseExecutor)) {
            log.warning("io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor are not provided");
            throw new IllegalArgumentException("You can't run " + method + " because you are not provided io.cat.ai.asyncpp.concurrent.program.executor.BaseExecutor");
        } else if (!isPipelineCreated) {
            log.warning("io.cat.ai.asyncpp.core.pipeline.AbstractPipeline instance not provided");
            throw new IllegalArgumentException("You can't run " + method + " because io.cat.ai.asyncpp.core.pipeline.AbstractPipeline instance does not exist! Please, create pipeline");
        }
    }

    private boolean isEntityAlreadyExistsInPipeline(String name) {
        checkBefore("isEntityAlreadyExistsInPipeline");

        for (val __: this.pipeline())
            return __.getKey().equals(name);

        return false;
    }

    static <T> Asyncpp<T> fromExecutor(BaseExecutor baseExecutor) {
        return new AsyncppImpl<>(baseExecutor);
    }

    @Override
    public Asyncpp<T> createPipeline() {
        if (!isPipelineCreated) {
            this.pipeline = new DefaultPipeline<>();
            this.isPipelineCreated = true;
        }
        return this;
    }

    @Override
    public Asyncpp<T> withPipeline(AbstractPipeline<T> pipeline) {
        if (!isPipelineCreated) {
            this.pipeline = pipeline;
            this.isPipelineCreated = true;
        } else {
            log.severe("You replaced existing pipeline");
        }
        return this;
    }

    @Override
    public AbstractPipeline<T> pipeline() {
        if (!isPipelineCreated)
            throw new RuntimeException("No io.cat.ai.asyncpp.core.pipeline.AbstractPipeline instance presented!");

        return this.pipeline;
    }

    @Override
    public Asyncpp<T> withEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier) {
        checkBefore("withEntity");

        if (!isEntityAlreadyExistsInPipeline(name)) {
            val entity = entitySupplier.get();
            this.entities.put(name, entity);
            this.pipeline.addLast(name, entity);

            setExecutorForEntity(name);
        }
        return this;
    }

    @Override
    public Asyncpp<T> withFirstEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier) {
        checkBefore("withFirstEntity");

        if (!isEntityAlreadyExistsInPipeline(name)) {
            val entity = entitySupplier.get();
            this.entities.put(name, entity);
            this.pipeline.addFirst(name, entity);

            setExecutorForEntity(name);
        }
        return this;
    }

    @Override
    public Asyncpp<T> withLastEntity(String name, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier) {
        checkBefore("withLastEntity");

        if (!isEntityAlreadyExistsInPipeline(name)) {
            val entity = entitySupplier.get();
            this.entities.put(name, entity);
            this.pipeline.addLast(name, entity);

            setExecutorForEntity(name);
        }
        return this;
    }

    @Override
    public Asyncpp<T> withEntityBefore(String entityNameBefore, String entityNameAfter, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier) {
        checkBefore("withEntityBefore");

        if (isEntityAlreadyExistsInPipeline(entityNameBefore)) {
            if (!isEntityAlreadyExistsInPipeline(entityNameAfter)) {
                val entity = entitySupplier.get();
                this.entities.put(entityNameAfter, entity);
                this.pipeline.addBefore(entityNameBefore, entityNameAfter, entity);

                setExecutorForEntity(entityNameAfter);
            }
        }
        return this;
    }

    @Override
    public Asyncpp<T> withEntityAfter(String entityName, String newEntityName, Supplier<AbstractBackgroundComputationEntity<T>> entitySupplier) {
        checkBefore("withEntityAfter");

        if (isEntityAlreadyExistsInPipeline(entityName)) {
            if (!isEntityAlreadyExistsInPipeline(newEntityName)) {
                val entity = entitySupplier.get();
                this.entities.put(newEntityName, entity);
                this.pipeline.addAfter(entityName, newEntityName, entity);

                setExecutorForEntity(newEntityName);
            }
        }
        return this;
    }

    @Override
    public Asyncpp<T> removeEntity(String entityName) {
        checkBefore("removeEntity");

        if (isEntityAlreadyExistsInPipeline(entityName)) {
            this.entities.remove(entityName);
            this.pipeline.remove(entityName);
        }
        return this;
    }

    @Override
    public Asyncpp<T> removeLastEntity() {
        checkBefore("removeLastEntity");
        this.pipeline.removeLast();
        return this;
    }

    @Override
    public Asyncpp<T> removeFirstEntity() {
        checkBefore("removeFirstEntity");
        this.pipeline.removeFirst();
        return this;
    }

    @Override
    public Asyncpp<T> printWholeProcessingPipeline() {
        checkBefore("printWholeProcessingPipeline");
        this.pipeline.printWholePipeline();
        return this;
    }

    @Override
    public Asyncpp<T> process(T t) {
        checkBefore("process");
        this.pipeline.run(t);
        return this;
    }

    @Override
    public Asyncpp<T> processOn(String name, T t) {
        checkBefore("processOn");
        this.pipeline.runOn(name, t);
        return this;
    }

    @Override
    public Asyncpp<T> processAsync(T t) {
        checkBefore("processAsync");
        this.pipeline.runAsync(t);
        return this;
    }

    @Override
    public Asyncpp<T> processAsyncOn(String name, T t) {
        checkBefore("processAsyncOn");
        this.pipeline.runAsyncOn(name, t);
        return this;
    }

    @Override
    public Asyncpp<T> processWithDelay(T t, Duration duration) {
        checkBefore("processWithDelay");
        this.pipeline.runWithDelay(t, duration);
        return this;
    }

    @Override
    public Asyncpp<T> processWithDelayOn(String name, T t, Duration duration) {
        checkBefore("processWithDelayOn");
        this.pipeline.runWithDelayOn(name, t, duration);
        return this;
    }

    @Override
    public Asyncpp<T> processBatch(Collection<T> all) {
        checkBefore("processBatch");
        this.pipeline.runBatch(all);
        return this;
    }

    @Override
    public Asyncpp<T> processBatchOn(String name, Collection<T> all) {
        checkBefore("processBatchOn");
        this.pipeline.runBatchOn(name, all);
        return this;
    }
}