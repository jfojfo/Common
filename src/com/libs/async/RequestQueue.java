package com.libs.async;

import com.libs.utils.LogUtils;

import java.util.LinkedList;

public class RequestQueue {

    private static final String TAG = "RequestQueue";
    private LinkedList<Object> queue;

    public RequestQueue() {
        queue = new LinkedList<Object>();
    }

    /**
     * check whether queue is empty
     */
    public synchronized boolean isEmpty() {
        return queue.size() == 0;
    }

    /**
     * how many request/response in queue
     */
    public synchronized int countPending() {
        return queue.size();
    }

    public synchronized void clearPending() {
        queue.clear();
    }

    /**
     * push this obj on the request queue
     */
    public synchronized void push(Object obj) {
        LogUtils.d("PUSH:" + obj.toString());
        queue.addLast(obj);
        // wakes up any waiting thread
        notify();
    }

    /**
     * pop up request/response
     */
    public synchronized Object pop()
            throws InterruptedException {
        LogUtils.d("POP:...");
        while (queue.size() <= 0) {
            try {
                // not using timeout currently
                wait();
            } catch (InterruptedException e) {
                throw e;
            }
        }
        return queue.removeFirst();
    }

    public synchronized Object peekFirst() {
        if (queue.size() > 0) {
            return queue.getFirst();
        } else {
            return null;
        }
    }
}
