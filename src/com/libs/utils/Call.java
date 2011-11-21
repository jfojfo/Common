package com.libs.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Call {

    public static Object call(String clazz, String method, Class[] argsType, Object[] args) {
        Object ret = null;
        
        try {
            Class c = Class.forName(clazz);
            Method m = c.getMethod(method, argsType);
            m.setAccessible(true);
            ret = m.invoke(null, args);
        } catch (ClassNotFoundException e) {
            Utils.handleException(e);
        } catch (SecurityException e) {
            Utils.handleException(e);
        } catch (NoSuchMethodException e) {
            Utils.handleException(e);
        } catch (IllegalArgumentException e) {
            Utils.handleException(e);
        } catch (IllegalAccessException e) {
            Utils.handleException(e);
        } catch (InvocationTargetException e) {
            Utils.handleException(e);
        }
        
        return ret;
    }

    public static Object call(Object obj, String method, Class[] argsType, Object[] args) {
        Object ret = null;
        
        if (obj == null)
            return ret;
        
        try {
            Class c = obj.getClass();
            Method m = c.getMethod(method, argsType);
            m.setAccessible(true);
            ret = m.invoke(obj, args);
        } catch (SecurityException e) {
            Utils.handleException(e);
        } catch (NoSuchMethodException e) {
            Utils.handleException(e);
        } catch (IllegalArgumentException e) {
            Utils.handleException(e);
        } catch (IllegalAccessException e) {
            Utils.handleException(e);
        } catch (InvocationTargetException e) {
            Utils.handleException(e);
        }
        
        return ret;
    }

}
