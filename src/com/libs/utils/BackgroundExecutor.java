package com.libs.utils;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class BackgroundExecutor {

    private static Executor executor = Executors.newCachedThreadPool();

    public static void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public static void setExecutor(Executor executor) {
        BackgroundExecutor.executor = executor;
    }

}