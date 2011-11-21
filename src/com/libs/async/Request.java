package com.libs.async;

import java.util.Observable;
import java.util.Observer;

public class Request extends Observable {
    private long _id;
    private int type;
    private boolean async = true; // ignore this flag
    private int status;
    private String errorMessage = null;
    private Object data;

    public Request(long _id, int type) {
        this._id = _id;
        this.type = type;
    }

    public Request(long _id, int type, boolean async) {
        this(_id, type);
        this.async = async;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public int getType() {
        return type;
    }

    public long getId() {
        return _id;
    }

    public int getStatus() {
        return status;
    }

    public boolean isAsync() {
        return async;
    }

    public void notifyObservers(Object data) {
        this.setChanged();
        super.notifyObservers(data);
    }

    public void setErrorMessage(String msg) {
        errorMessage = msg;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @Override
    public void addObserver(Observer observer) {
        if (observer != null)
            super.addObserver(observer);
    }
    
}
