package com.ktrueda.npegah.fieldfilter;


import java.lang.reflect.Field;

/**
 * All filed of target class will be assigned some values.
 */
public class AllFieldFilter implements FieldFilter {
    @Override
    public boolean filter(Class clz, Field f) {
        return true;
    }
}
