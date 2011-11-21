package com.libs.async;

import java.util.Observable;
import java.util.Observer;

public abstract class BaseService {

    public static final String TAG = "BaseService";
    private Observable mWatcher;

    protected BaseService() {
        mWatcher = new MyObservable();
    }
    
    public void registerObserver(Observer observer) {
        mWatcher.addObserver(observer);
    }
    
    public void unregisterObserver(Observer observer) {
        mWatcher.deleteObserver(observer);
    }
    
    protected void notifyObservers(Object data) {
        mWatcher.notifyObservers(data);
    }

    
    private static class MyObservable extends Observable {
        public void notifyObservers(Object data) {
            this.setChanged();
            super.notifyObservers(data);
        }
    }

}
