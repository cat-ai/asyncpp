package io.cat.ai.asyncpp.core.pipeline;

import io.cat.ai.asyncpp.core.computation.AbstractBackgroundComputationEntity;
import io.cat.ai.asyncpp.core.manager.ComputationManager;
import io.cat.ai.asyncpp.core.manager.component.ComputationWrapper;

import lombok.val;
import lombok.var;

import java.time.Duration;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.CompletableFuture;

import static java.util.Objects.*;

public final class DefaultPipeline<T> extends AbstractPipeline<T> {

    @Override
    public void run(T t) {
        var nextInPipeline = head;

        while (nextInPipeline != null) {
            nextInPipeline.run(t);
            nextInPipeline = nextInPipeline.next;
        }
    }

    @Override
    public void runOn(String name, T t) {
        var nextInPipeline = head;

        while (nonNull(nextInPipeline)) {
            if (nextInPipeline.name.equals(name)) {
                nextInPipeline.run(t);
                return;
            }
            else
                nextInPipeline = nextInPipeline.next;
        }
    }

    @Override
    public void runWithDelay(T t, Duration duration) {
        var nextInPipeline = head;

        while (nextInPipeline != null) {
            nextInPipeline.runWithDelay(t, duration);
            nextInPipeline = nextInPipeline.next;
        }
    }

    @Override
    public void runWithDelayOn(String name, T t, Duration duration) {
        var nextInPipeline = head;

        while (nextInPipeline != null) {
            if (nextInPipeline.name.equals(name)) {
                nextInPipeline.runWithDelay(t, duration);
                return;
            } else
                nextInPipeline = nextInPipeline.next;
        }
    }

    @Override
    public void runBatch(Collection<T> all) {
        var nextInPipeline = head;

        while (nonNull(nextInPipeline)) {
            nextInPipeline.runBatch(all);
            nextInPipeline = nextInPipeline.next;
        }
    }

    @Override
    public void runBatchOn(String name, Collection<T> all) {
        var nextInPipeline = head;

        while (nonNull(nextInPipeline)) {
            if (nextInPipeline.name.equals(name)) {
                nextInPipeline.runBatch(all);
                return;
            }
            else
                nextInPipeline = nextInPipeline.next;
        }
    }

    @Override
    public void runAsync(T t) {
        var nextInPipeline = head;

        while (nextInPipeline != null) {
            nextInPipeline.runAsync(CompletableFuture.supplyAsync(() -> t, nextInPipeline.entity.executor()));
            nextInPipeline = nextInPipeline.next;
        }
    }

    @Override
    public void runAsyncOn(String name, T t) {
        var nextInPipeline = head;

        while (nonNull(nextInPipeline)) {
            if (nextInPipeline.name.equals(name)) {
                nextInPipeline.runAsync(CompletableFuture.supplyAsync(() -> t, nextInPipeline.entity.executor()));
                return;
            }
            else
                nextInPipeline = nextInPipeline.next;
        }
    }

    private String wholePipelineAsString() {
        val buffer = new StringBuilder();
        var node = this.head;

        buffer.append("Pipeline [ ");

        while (nonNull(node)) {
            buffer.append(node.name).append(" *--* ");
            node = node.next;
        }

        buffer.append(" Nil ]");

        return buffer.toString();
    }

    @Override
    public AbstractPipeline<T> printWholePipeline() {
        System.out.println(wholePipelineAsString());

        return this;
    }

    @Override
    public ComputationManager<ComputationWrapper> computationManager() {
        return context().entity.computationManager();
    }

    @Override
    public int length() {
        var nextInPipeline = head;
        var len = 0;

        while (nonNull(nextInPipeline)) {
            len++;
            nextInPipeline = nextInPipeline.next;
        }
        return len;
    }

    public AbstractPipelineContext<T> context() {
        return this.head;
    }

    public AbstractPipeline<T> addLast(String name, AbstractBackgroundComputationEntity<T> action) {
        val newContext = new DefaultPipelineContext<>(name, action);
        var last = head;

        newContext.next = null;

        if (isNull(head)) {
            newContext.prev = null;
            head = newContext;
            return this;
        }

        while (nonNull(last.next))
            last = last.next;

        last.next = newContext;
        newContext.prev = last;

        return this;
    }

    public AbstractPipeline<T> addFirst(String name, AbstractBackgroundComputationEntity<T> action) {
        val newContext = new DefaultPipelineContext<T>(name, action);

        newContext.next = head;
        newContext.prev = null;

        if (nonNull(head))
            head.prev = newContext;

        this.head = newContext;

        return this;
    }

    public AbstractPipeline<T> remove(String name) {
        var nextInPipeline = head;
        AbstractPipelineContext<T> prev = null;

        while (nonNull(nextInPipeline)) {
            if (nextInPipeline.name.equals(name)) {

                if (isNull(prev))
                    this.head = nextInPipeline.next;
                else
                    prev.next = nextInPipeline.next;

                break;
            } else {
                prev = nextInPipeline;
                nextInPipeline = nextInPipeline.next;
            }
        }
        return this;
    }

    public AbstractPipeline<T> removeFirst() {
        var nextInPipeline = head;

        if (isNull(nextInPipeline))
            return this;

        this.head = nextInPipeline.next;

        return this;
    }

    public AbstractPipeline<T> removeLast() {
        var nextInPipeline = head;

        if (isNull(nextInPipeline) || isNull(nextInPipeline.next))
            return this;

        while (nonNull(nextInPipeline.next.next))
            nextInPipeline = nextInPipeline.next;

        nextInPipeline.next = null;

        return this;
    }

    public AbstractPipeline<T> addAfter(String prevName, String newName, AbstractBackgroundComputationEntity<T> newAction) {
        var nextInPipeline = head;

        while (nonNull(nextInPipeline)) {
            if (nextInPipeline.name.equals(prevName)) {

                val newContext = new DefaultPipelineContext<>(newName, newAction);

                newContext.next = nextInPipeline.next;
                nextInPipeline.next = newContext;

                newContext.prev = nextInPipeline;

                if (nonNull(newContext.next))
                    newContext.next.prev = newContext;

                return this;
            } else
                nextInPipeline = nextInPipeline.next;
        }
        return this;
    }

    public AbstractPipeline<T> addBefore(String prevName, String newName, AbstractBackgroundComputationEntity<T> newEntity) {
        var nextInPipeline = head;

        while (nonNull(nextInPipeline.next)) {
            if (nextInPipeline.next.name.contains(prevName)) {
                val newContext = new DefaultPipelineContext<>(newName, newEntity);
                newContext.next = nextInPipeline.next;
                nextInPipeline.next = newContext;
                return this;
            }
            nextInPipeline = nextInPipeline.next;
        }
        return this;
    }

    @Override
    public Iterator<Map.Entry<String, AbstractBackgroundComputationEntity<T>>> iterator() {
        return new Iterator<Map.Entry<String, AbstractBackgroundComputationEntity<T>>>() {

            private AbstractPipelineContext<T> current = head;
            private AbstractPipelineContext<T> lastAccessed = null;
            private int idx = 0;
            private int n = length();

            @Override
            public boolean hasNext() {
                return idx < n;
            }

            @Override
            public Map.Entry<String, AbstractBackgroundComputationEntity<T>> next() {

                if (!hasNext())
                    throw new NoSuchElementException();

                this.lastAccessed = this.current;

                val cur = this.current;

                val last = new Map.Entry<String, AbstractBackgroundComputationEntity<T>>() {

                    @Override
                    public String getKey() {
                        return cur.name;
                    }

                    @Override
                    public AbstractBackgroundComputationEntity<T> getValue() {
                        return cur.entity;
                    }

                    @Override
                    public AbstractBackgroundComputationEntity<T> setValue(AbstractBackgroundComputationEntity<T> __) {
                        return getValue();
                    }
                };

                this.current = cur.next;
                this.idx++;

                return last;
            }
        };
    }
}