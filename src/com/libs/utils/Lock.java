package com.libs.utils;

import java.util.concurrent.atomic.AtomicInteger;

public class Lock {
    private AtomicInteger atomicInt = new AtomicInteger(0);

    public boolean enter() {
        return atomicInt.compareAndSet(0, 1);
    }
    
    public void exit() {
        atomicInt.set(0);
    }
    
}
