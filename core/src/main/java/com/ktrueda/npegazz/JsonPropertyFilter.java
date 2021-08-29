package com.ktrueda.npegazz;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.lang.reflect.Field;

public class JsonPropertyFilter implements FieldFilter{
    @Override
    public boolean filter(Class clz, Field f) {
        return f.getDeclaredAnnotationsByType(JsonProperty.class).length > 0;
    }
}
