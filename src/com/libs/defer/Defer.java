package com.libs.defer;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Defer {

    public interface Func {
        void call(Object... args);
    }

    private List<Func> mDoneQueue, mFailQueue, mProgressQueue;
    private int mState;
    private Object[] mArgsDone, mArgsFail, mArgsProgress;
    private static final int STATE_IDLE = 0;
    private static final int STATE_DONE = 1;
    private static final int STATE_FAIL = 2;
    private static final int STATE_PROGRESS = 3;

    public static class Promise {
        private Defer mDefer;

        public Promise(Defer defer) {
            mDefer = defer;
        }

        public Promise done(Func cb) {
            mDefer.done(cb);
            return this;
        }

        public Promise fail(Func cb) {
            mDefer.fail(cb);
            return this;
        }
        
        public Promise progress(Func cb) {
            mDefer.progress(cb);
            return this;
        }
        
    }

    public Defer() {
        mDoneQueue = new LinkedList<Func>();
        mFailQueue = new LinkedList<Func>();
        mProgressQueue = new LinkedList<Func>();
        mState = STATE_IDLE;
    }

    public Promise promise() {
        return new Promise(this);
    }

    synchronized public void done(Func cb) {
        if (cb != null) {
            if (mState == STATE_DONE) {
                cb.call(mArgsDone);
            } else {
                mDoneQueue.add(cb);
            }
        }
    }

    synchronized public void fail(Func cb) {
        if (cb != null) {
            if (mState == STATE_FAIL) {
                cb.call(mArgsFail);
            } else {
                mFailQueue.add(cb);
            }
        }
    }

    synchronized public void progress(Func cb) {
        if (cb != null) {
            mProgressQueue.add(cb);
            if (mState == STATE_PROGRESS) {
                cb.call(mArgsProgress);
            }
        }
    }

    synchronized public void resolve(Object... args) {
        mArgsDone = args;
        mState = STATE_DONE;
        _action(args);
    }

    synchronized public void reject(Object... args) {
        mArgsFail = args;
        mState = STATE_FAIL;
        _action(args);
    }

    synchronized public void notify(Object... args) {
        mArgsProgress = args;
        mState = STATE_PROGRESS;
        _action(args);
    }
    
    protected void _action(Object... args) {
        List<Func> queue = null;
        if (mState == STATE_DONE) {
            queue = mDoneQueue;
        } else if (mState == STATE_FAIL) {
            queue = mFailQueue;
        } else if (mState == STATE_PROGRESS) {
            queue = mProgressQueue;
        }
        if (queue == null)
            return;
        Iterator<Func> iter = queue.iterator();
        while (iter.hasNext()) {
            Func cb = iter.next();
            cb.call(args);
        }
    }

}