package com.ktrueda.npegaz;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DefaultValueSelector implements FieldValueSelector {
    static class DefaultValue {
        static final Set<String> STRING = new HashSet<String>() {{
            add("string-value");
            add(null);
        }};
        static final HashSet<ArrayList<String>> STRING_ARRAY = new HashSet<ArrayList<String>>() {{
            add(new ArrayList<String>() {{
                add("string-value");
            }});
            add(new ArrayList<>());
            add(null);
        }};
        static final Set<Boolean> BOOLEAN = new HashSet<Boolean>() {{
            add(true);
            add(null);
        }};
        static final HashSet<ArrayList<Boolean>> BOOLEAN_ARRAY = new HashSet<ArrayList<Boolean>>() {{
            add(new ArrayList<Boolean>() {{
                add(true);
            }});
            add(new ArrayList<>());
            add(null);
        }};
        static final HashSet<Integer> INTEGER = new HashSet<Integer>() {{
            add(123);
            add(null);
        }};
        static final HashSet<ArrayList<Integer>> INTEGER_ARRAY = new HashSet<ArrayList<Integer>>() {{
            add(new ArrayList<Integer>() {{
                add(123);
            }});
            add(new ArrayList<>());
            add(null);
        }};

        static final HashSet<Float> FLOAT = new HashSet<Float>() {{
            add(3.14f);
            add(null);
        }};
        static final HashSet<ArrayList<Float>> FLOAT_ARRAY = new HashSet<ArrayList<Float>>() {{
            add(new ArrayList<Float>() {{
                add(3.14f);
            }});
            add(new ArrayList<>());
            add(null);
        }};

        static final HashSet<Double> DOUBLE = new HashSet<Double>() {{
            add(2.718d);
            add(null);
        }};
        static final HashSet<ArrayList<Double>> DOUBLE_ARRAY = new HashSet<ArrayList<Double>>() {{
            add(new ArrayList<Double>() {{
                add(2.718d);
            }});
            add(new ArrayList<>());
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

    static boolean classEq(Class cls1, Class cls2) {
        return cls1.getCanonicalName().equals(cls2.getCanonicalName());
    }
}
