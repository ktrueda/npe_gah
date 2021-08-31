package com.ktrueda.npegah.generator;

import java.util.HashSet;
import java.util.Set;

/**
 * Basic value generator
 */
public class BasicValueGenerator implements FieldValueGenerator {
    static class DefaultValue {
        static final Set<String> STRING = new HashSet<String>() {{
            add("string-value");
            add(null);
        }};
        static final HashSet<String[]> STRING_ARRAY = new HashSet<String[]>() {{
            add(new String[]{"string-value"});
            add(new String[]{});
            add(null);
        }};
        static final Set<Boolean> BOOLEAN = new HashSet<Boolean>() {{
            add(true);
            add(null);
        }};
        static final HashSet<Boolean[]> BOOLEAN_ARRAY = new HashSet<Boolean[]>() {{
            add(new Boolean[]{true});
            add(new Boolean[]{});
            add(null);
        }};
        static final HashSet<Integer> INTEGER = new HashSet<Integer>() {{
            add(123);
            add(null);
        }};
        static final HashSet<Integer[]> INTEGER_ARRAY = new HashSet<Integer[]>() {{
            add(new Integer[]{123});
            add(new Integer[]{});
            add(null);
        }};
        static final HashSet<Float> FLOAT = new HashSet<Float>() {{
            add(3.14f);
            add(null);
        }};
        static final HashSet<Float[]> FLOAT_ARRAY = new HashSet<Float[]>() {{
            add(new Float[]{3.14f});
            add(new Float[]{});
            add(null);
        }};

        static final HashSet<Double> DOUBLE = new HashSet<Double>() {{
            add(2.718d);
            add(null);
        }};
        static final HashSet<Double[]> DOUBLE_ARRAY = new HashSet<Double[]>() {{
            add(new Double[]{2.718d});
            add(new Double[]{});
            add(null);
        }};

    }

    @Override
    public <T> Set<T> value(Class<T> clz) {
        if (clz.isArray()) {
            if (classEq(clz.getComponentType(), String.class)) {
                return (HashSet<T>) DefaultValue.STRING_ARRAY;
            } else if (classEq(clz.getComponentType(), Integer.class)) {
                return (HashSet<T>) DefaultValue.INTEGER_ARRAY;
            } else if (classEq(clz.getComponentType(), Float.class)) {
                return (HashSet<T>) DefaultValue.FLOAT_ARRAY;
            } else if (classEq(clz.getComponentType(), Double.class)) {
                return (HashSet<T>) DefaultValue.DOUBLE_ARRAY;
            } else if (classEq(clz.getComponentType(), Boolean.class)) {
                return (HashSet<T>) DefaultValue.BOOLEAN_ARRAY;
            }
        } else if (classEq(clz, String.class)) {
            return (HashSet<T>) DefaultValue.STRING;
        } else if (classEq(clz, Integer.class)) {
            return (HashSet<T>) DefaultValue.INTEGER;
        } else if (classEq(clz, Double.class)) {
            return (HashSet<T>) DefaultValue.DOUBLE;
        } else if (classEq(clz, Float.class)) {
            return (HashSet<T>) DefaultValue.FLOAT;
        } else if (classEq(clz, Boolean.class)) {
            return (HashSet<T>) DefaultValue.BOOLEAN;
        }
        return null;
    }

    private static boolean classEq(Class cls1, Class cls2) {
        return cls1.getCanonicalName().equals(cls2.getCanonicalName());
    }
}
