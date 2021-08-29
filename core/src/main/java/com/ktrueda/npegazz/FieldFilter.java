package com.ktrueda.npegazz;

import java.lang.reflect.Field;

public interface FieldFilter {
    boolean filter(Class clz, Field f);
}
