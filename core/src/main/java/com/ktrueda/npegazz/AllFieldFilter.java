package com.ktrueda.npegazz;


import java.lang.reflect.Field;

public class AllFieldFilter implements FieldFilter {
    @Override
    public boolean filter(Class clz, Field f) {
        return true;
    }
}
