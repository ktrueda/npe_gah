package com.ktrueda.npegah.fieldfilter;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;

/**
 * Jackson field selector.
 * <p>
 * For example, if you use JSON as system input (e.g. HTTP) you shall use Jackson to serialize/deserialize input.
 * Then you need to run fuzz test to that kind of input, you can use this filter.
 */
public class JsonPropertyFilter implements FieldFilter {
    @Override
    public boolean filter(Class clz, Field f) {
        return f.getDeclaredAnnotationsByType(JsonProperty.class).length > 0;
    }
}
