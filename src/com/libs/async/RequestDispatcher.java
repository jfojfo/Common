package com.libs.async;

public interface RequestDispatcher {
    void dispatch(Request request);
    void pushRequest(Request request);
    Request popRequest(int id) throws InterruptedException;
}
