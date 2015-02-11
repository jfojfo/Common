package com.libs.utils;

import java.lang.reflect.Field;

public class FieldHelper<T> {
    private Object obj = null;
    private Class<?> clazz;
    private String fieldName;

    private boolean inited;
    private Field field;

    public FieldHelper(String fieldName) {
        this.fieldName = fieldName;
    }

    public FieldHelper<T> obj(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException("obj cannot be null");
        }
        this.obj = obj;
        return this;
    }

    public FieldHelper<T> clazz(Class<?> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("class cannot be null");
        }
        this.clazz = clazz;
        return this;
    }

    private void prepare() {
        if (inited)
            return;
        inited = true;

        if (obj != null)
            clazz = obj.getClass();
        while (clazz != null) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                field = f;
                return;
            } catch (Exception e) {
            } finally {
                clazz = clazz.getSuperclass();
            }
        }
    }

    public T get() throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();

        if (field == null)
            throw new NoSuchFieldException();

        try {
            @SuppressWarnings("unchecked")
            T r = (T) field.get(obj);
            return r;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("unable to cast object");
        }
    }

    public void set(T val) throws NoSuchFieldException, IllegalAccessException, IllegalArgumentException {
        prepare();

        if (field == null)
            throw new NoSuchFieldException();

        field.set(obj, val);
    }
}
