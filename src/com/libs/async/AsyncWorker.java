package com.libs.async;

import android.os.Process;

public class AsyncWorker extends Thread {
    private int mThreadId;
    private boolean mRunning = true;
    private RequestDispatcher mDispatcher;

    public AsyncWorker(RequestDispatcher dispatcher, int id) {
        this(dispatcher, id, "AsyncWorker");
    }

    public AsyncWorker(RequestDispatcher dispatcher, int id, String name) {
        super(name + "#" + id);
        mThreadId = id;
        mDispatcher = dispatcher;
    }

    public void quit() {
        mRunning = false;
    }
    
    public void run() {
        // don't use Thread.setPriority(...)!!!
        // setPriority(Process.THREAD_PRIORITY_BACKGROUND); 
        // set in run() !!! do *not* set in Constructors!
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
        while (mRunning) {
            Request request = null;
            try {
                request = (Request) mDispatcher.popRequest(mThreadId);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            if (request == null) {
                // nothing to do, just yield ...
                // don't let the thread has chance to spin
                Thread.yield();
                continue;
            }

            mDispatcher.dispatch(request);
        }
    }

}
