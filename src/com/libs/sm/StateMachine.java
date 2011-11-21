package com.libs.sm;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import android.util.Log;

public class StateMachine {
    private static final String TAG = StateMachine.class.getName();
    public static final int FLAG_NEXT_STATE_SPECIFIED = 0x1;
    private SMState mCurrentState;
    private SMEvent mEventGenerator;

    public StateMachine() {
        mCurrentState = null;
        mEventGenerator = new SMEvent();
    }

    public SMState init() {
        mCurrentState = new SMState();
        return mCurrentState;
    }

    public SMState init(SMState initState) {
        mCurrentState = initState;
        return mCurrentState;
    }

    public SMState registerTransition(SMState current, SMEvent e) {
        if (null == current || null == e) {
            Log.e(TAG, "registerTransition(): null parameter");
            return null;
        }
        Log.d(TAG, "register transition:" + current.getDescription() + "->" + e.getDescription() + "->...");
        SMState nextState = SMState.newState();
        current.addTransition(e, nextState);
        return nextState;
    }

    public void registerTransition(SMState current, SMEvent e, SMState next) {
        if (null == current || null == e || null == next) {
            Log.e(TAG, "registerTransition(): null parameter");
            return;
        }
        Log.d(TAG, "register transition:" + current.getDescription() + "->" + e.getDescription()
                + "->" + next.getDescription());
        current.addTransition(e, next);
    }

    public SMState fireEvent(SMEvent e) {
        if (null == mCurrentState) {
            Log.e(TAG, "StateMachine not initialized yet!");
            return null;
        }
        if (null == e) {
            Log.e(TAG, "fireEvent(): null parameter");
            return null;
        }
        
        Log.d(TAG, "fireEvent:" + e.getDescription() + "|state:" + mCurrentState.getDescription());
        
        SMState nextState = null;
        if ((e.getFlag() & FLAG_NEXT_STATE_SPECIFIED) != 0) {
            nextState = (SMState)e.getArgs();
            Log.d(TAG, "transite:" + mCurrentState.getDescription() + 
                    "->" + e.getDescription() + "[ChangeState]" + "->" + nextState.getDescription());
        }
        else {
            nextState = mCurrentState.transite(e);
        }
        if (null == nextState)
            // this is a transition that should not happen
            return null;
        SMState oldState = changeState(nextState);
        handleEvent(oldState, e, nextState);
        
        return nextState;
    }

    private SMState changeState(SMState newState) {
        SMState oldState = mCurrentState;
        mCurrentState = newState;
        return oldState;
    }

    public void handleEvent(SMState currState, SMEvent e, SMState nextState) {
        if (null != e.cb) {
            e.setTransitionId(currState);
            e.cb.onEvent(this, e);
        }
    }
    
    public void onEvent(SMEvent e) {
        Log.e(TAG, "Unknown event:" + e.getDescription() + "|transitionId:0x" + Integer.toHexString((Integer)e.getTransitionId()));
    }

    public SMEvent genEvent() {
        return mEventGenerator.nextEvent();
    }

    public SMEvent genEvent(String desc) {
        SMEvent e = genEvent();
        e.setDescription(desc);
        return e;
    }

    static public class SMState {
        private Integer id;
        private Map<SMEvent,SMState> mState;
        private String mDescription;

        public SMState() {
            mState = Collections.synchronizedMap(new LinkedHashMap<SMEvent,SMState>());
        }

        public SMState(String desc) {
            this();
            mDescription = desc;
        }

        public SMState(Integer id, String desc) {
            this();
            this.id = id;
            this.mDescription = desc;
        }

        static private SMState newState() {
            return new SMState();
        }

        private void addTransition(SMEvent e, SMState next) {
            if (mState.get(e) != null) {
                Log.v(TAG, "Transition already exists:" +
                        this.getDescription() + "->" +
                        e.getDescription() + "->" +
                        mState.get(e).getDescription() +
                        "|newState:" + next.getDescription());
            }
            mState.put(e, next);
        }

        private SMState transite(SMEvent e) {
            SMState next = null;
            if ((e.getFlag() & FLAG_NEXT_STATE_SPECIFIED) != 0)
                next = (SMState) e.getArgs();
            else
                next = mState.get(e);
            
            if (null == next) {
                Log.v(TAG, "Unknown transition:" +
                        this.getDescription() + "->" + 
                        e.getDescription() + "->???");
                return null;
            }
            Log.d(TAG, "transite:" + this.getDescription() + "->" + e.getDescription() + "->" + next.getDescription());
            return next;
        }

        private String getDescription() {
            return mDescription == null ?
                this.toString() : mDescription;
        }

        public void setDescription(String desc) {
            mDescription = desc;
        }

        public Integer getID() {
            return id;
        }
        
        public void setID(Integer id) {
            this.id = id;
        }
        
    }

    static public class SMEvent {
        private Integer id;
        private SMCallback cb;
        private String mDescription;
        private Object mArgs;
        private int mFlag;
        private Object mTransitionId;

        public SMEvent() {
            this(0);
        }

        public SMEvent(Integer i) {
            id = i;
        }

        public SMEvent(Integer i, SMCallback cb) {
            this.id = i;
            this.cb = cb;
        }

        public SMEvent(Integer i, SMCallback cb, String desc) {
            this.id = i;
            this.cb = cb;
            this.mDescription = desc;
        }

        public SMEvent nextEvent() {
            return new SMEvent(id + 1);
        }

        public SMCallback getCallback() {
            return this.cb;
        }

        public void setCallback(SMCallback cb) {
            this.cb = cb;
        }

        public Integer getID() {
            return id;
        }
        
        public void setID(Integer id) {
            this.id = id;
        }
        
        public String getDescription() {
            return mDescription == null ?
                this.toString() : mDescription;
        }

        public void setDescription(String desc) {
            mDescription = desc;
        }
        
        public int setFlag(int flag) {
            int old = mFlag;
            mFlag = flag;
            return old;
        }
        
        public int addFlag(int flag) {
            int old = mFlag;
            mFlag |= flag;
            return old;
        }
        
        public int getFlag() {
            return mFlag;
        }

        public void setArgs(Object o) {
            mArgs = o;
        }
        
        public Object getArgs() {
            return mArgs;
        }

        public void setTransitionId(Object transitionId) {
            mTransitionId = transitionId;
        }
        
        public Object getTransitionId() {
            return mTransitionId;
        }
    }

    public interface SMCallback {
        public void onEvent(StateMachine sm, SMEvent e);
    }

}
