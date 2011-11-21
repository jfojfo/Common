package com.libs.async;

import java.util.Observable;
import java.util.Observer;

import android.os.Handler;
import android.os.Message;

public class RequestHandler extends Handler {

    public static final int MSG_HANDLE_REQUEST = 0x7FFFFFFE;

    @Override
    public void dispatchMessage(Message msg) {
        if (msg.what == MSG_HANDLE_REQUEST)
            handleMessageInternal(msg);
        super.dispatchMessage(msg);
    }

    protected void handleMessageInternal(Message msg) {
        switch (msg.what) {
        case MSG_HANDLE_REQUEST: {
            Object[] data = (Object[]) msg.obj;
            Request req = (Request) data[0];
            Object result = data[1];
            handleRequest(req, result);
            break;
        }
        default:
            break;
        }
    }

    public void handleRequest(Request req, Object result) {
        if (req.getStatus() == AsyncServiceConstants.STATUS_SUCCESS)
            handleRequestSuccess(req, result);
        else
            handleRequestFail(req);
    }
    
    public void handleRequestSuccess(Request req, Object result) {
        
    }
    
    public void handleRequestFail(Request req) {
        
    }
    
    
    private Observer callback = new Observer() {
        public void update(Observable observable, Object result) {
            Request req = (Request) observable;
            Object data = new Object[] {req, result};
            Message.obtain(RequestHandler.this, MSG_HANDLE_REQUEST, data).sendToTarget();
        }
    };

    public Observer getCallback() {
        return callback;
    }

    public void setCallback(Observer callback) {
        this.callback = callback;
    }

}
