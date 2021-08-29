package com.ktrueda.npegah.fieldfilter;

import java.lang.reflect.Field;

/**
 * FieldFilter.
 * <p>
 * You can define filed filter by this interface.
 * Only field which this.filter()'s return is true will be assigned some value.
 * Otherwise null will be assigned to the field.
 */
public interface FieldFilter {
    boolean filter(Class clz, Field f);
}
