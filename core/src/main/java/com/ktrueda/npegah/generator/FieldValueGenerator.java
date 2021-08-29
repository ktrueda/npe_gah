package com.ktrueda.npegah.generator;

import java.util.Set;

/**
 * Generating value for each class.
 */
public interface FieldValueGenerator {
    <T> Set<T> value(Class<T> clz);
}
