package io.cat.ai.asyncpp.concurrent.program.executor.utils;

import lombok.*;

import java.util.concurrent.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ProgramExecutorUtils {

    private static final String WORKER_THREAD_NAME_PREFIX = "asyncpp-async-fork-join-pool-";
    private static final String DELAYED_THREAD_NAME = "asyncpp-delayed-executor";
    private static final String SINGLE_THREAD_NAME = "asyncpp-single-executor";
    private static final String CACHED_THREAD_NAME = "asyncpp-cached-executor-";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class JavaConcurrentApi {

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class ExecutorServices {

            private static Thread newDaemonThread(Runnable runnable, String name) {
                val thread = new Thread(runnable, name);
                thread.setDaemon(true);
                return thread;
            }

            public static ScheduledExecutorService delayed() {
                return Executors.newSingleThreadScheduledExecutor((runnable) -> newDaemonThread(runnable, DELAYED_THREAD_NAME));
            }

            public static ExecutorService single() {
                return Executors.newSingleThreadExecutor((runnable) -> newDaemonThread(runnable, SINGLE_THREAD_NAME));
            }

            public static ExecutorService fixed(int nThreads) {
                return Executors.newFixedThreadPool(nThreads);
            }

            public static ExecutorService cached() {
                return Executors.newCachedThreadPool((runnable) -> newDaemonThread(runnable, CACHED_THREAD_NAME));
            }

            public static AbstractExecutorService defaultForkJoinPool() {
                Thread.UncaughtExceptionHandler exceptionHandler = (__, cause) -> cause.printStackTrace();

                ForkJoinPool.ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory = pool -> {
                    val worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                    worker.setDaemon(true);
                    worker.setName(WORKER_THREAD_NAME_PREFIX + worker.getPoolIndex());
                    return worker;
                };

                return new ForkJoinPool(Runtime.getRuntime().availableProcessors(), forkJoinWorkerThreadFactory, exceptionHandler, true);
            }

            public static AbstractExecutorService forkJoinPool(int parallelism,
                                                               boolean asyncMode,
                                                               boolean isWorkerThreadDaemon,
                                                               String workerThreadFactoryName,
                                                               Thread.UncaughtExceptionHandler exceptionHandler) {
                ForkJoinPool.ForkJoinWorkerThreadFactory forkJoinWorkerThreadFactory = pool -> {
                    val worker = ForkJoinPool.defaultForkJoinWorkerThreadFactory.newThread(pool);
                    worker.setDaemon(isWorkerThreadDaemon);
                    worker.setName(WORKER_THREAD_NAME_PREFIX + workerThreadFactoryName + worker.getPoolIndex());
                    return worker;
                };

                return new ForkJoinPool(parallelism, forkJoinWorkerThreadFactory, exceptionHandler, asyncMode);
            }
        }
    }
}