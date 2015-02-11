package com.libs.utils;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Call {

    public static Object call(String clazz, String method, Class<?>[] argsType, Object[] args) {
        Object ret = null;
        
        try {
            Class<?> c = Class.forName(clazz);
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

    public static Object call(Object obj, String method, Class<?>[] argsType, Object[] args) {
        Object ret = null;
        
        if (obj == null) {
            throw new IllegalArgumentException("obj cannot be null");
        }
        
        try {
            Class<?> c = obj.getClass();
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

    public static <T> T getField(Object obj, String field) {
        FieldHelper<T> helper = new FieldHelper<T>(field).obj(obj);
        try {
            return helper.get();
        } catch (NoSuchFieldException e) {
            Utils.handleException(e);
        } catch (IllegalAccessException e) {
            Utils.handleException(e);
        } catch (IllegalArgumentException e) {
            Utils.handleException(e);
        }
        return null;
    }

    public static <T> T setField(Object obj, String field, T newValue) {
        FieldHelper<T> helper = new FieldHelper<T>(field).obj(obj);
        T oldValue = null;
        try {
            oldValue = helper.get();
            helper.set(newValue);
        } catch (NoSuchFieldException e) {
            Utils.handleException(e);
        } catch (IllegalAccessException e) {
            Utils.handleException(e);
        } catch (IllegalArgumentException e) {
            Utils.handleException(e);
        }
        return oldValue;
    }

    public static <T> T getField(String clazz, String field) {
        try {
            FieldHelper<T> helper = new FieldHelper<T>(field).clazz(Class.forName(clazz));
            return helper.get();
        } catch (NoSuchFieldException e) {
            Utils.handleException(e);
        } catch (IllegalAccessException e) {
            Utils.handleException(e);
        } catch (IllegalArgumentException e) {
            Utils.handleException(e);
        } catch (ClassNotFoundException e) {
            Utils.handleException(e);
        }
        return null;
    }

    public static <T> T setField(String clazz, String field, T newValue) {
        T oldValue = null;
        try {
            FieldHelper<T> helper = new FieldHelper<T>(field).clazz(Class.forName(clazz));
            oldValue = helper.get();
            helper.set(newValue);
        } catch (NoSuchFieldException e) {
            Utils.handleException(e);
        } catch (IllegalAccessException e) {
            Utils.handleException(e);
        } catch (IllegalArgumentException e) {
            Utils.handleException(e);
        } catch (ClassNotFoundException e) {
            Utils.handleException(e);
        }
        return oldValue;
    }

}
