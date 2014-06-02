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
    private Object mTag;

    public static class Promise {
        private Defer mDefer;

        private Promise(Defer defer) {
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

        public Promise always(Func cb) {
            mDefer.always(cb);
            return this;
        }

        public Promise then(Func cbDone) {
            mDefer.then(cbDone);
            return this;
        }

        public Promise then(Func cbDone, Func cbFail) {
            mDefer.then(cbDone, cbFail);
            return this;
        }

        public Promise then(Func cbDone, Func cbFail, Func cbProgress) {
            mDefer.then(cbDone, cbFail, cbProgress);
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

    protected void done(Func cb) {
        then(cb, null, null);
    }

    protected void fail(Func cb) {
        then(null, cb, null);
    }

    protected void progress(Func cb) {
        then(null, null, cb);
    }

    protected void always(Func cb) {
        then(cb, cb, null);
    }

    protected void then(Func cbDone) {
        then(cbDone, null, null);
    }

    protected void then(Func cbDone, Func cbFail) {
        then(cbDone, cbFail, null);
    }

    synchronized protected void then(Func cbDone, Func cbFail, Func cbProgress) {
        if (cbDone != null) {
            if (mState == STATE_DONE) {
                cbDone.call(mArgsDone);
            } else {
                mDoneQueue.add(cbDone);
            }
        }
        if (cbFail != null) {
            if (mState == STATE_FAIL) {
                cbFail.call(mArgsFail);
            } else {
                mFailQueue.add(cbFail);
            }
        }
        if (cbProgress != null) {
            mProgressQueue.add(cbProgress);
            if (mState == STATE_PROGRESS) {
                cbProgress.call(mArgsProgress);
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

    public Object getTag() {
        return mTag;
    }

    public void setTag(Object tag) {
        this.mTag = tag;
    }

}