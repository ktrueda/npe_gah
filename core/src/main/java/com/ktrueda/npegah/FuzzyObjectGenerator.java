package com.ktrueda.npegah;

import com.google.common.annotations.VisibleForTesting;
import com.ktrueda.npegah.exception.GahAssignFailedException;
import com.ktrueda.npegah.exception.GahInstantiationException;
import com.ktrueda.npegah.fieldfilter.AllFieldFilter;
import com.ktrueda.npegah.fieldfilter.FieldFilter;
import com.ktrueda.npegah.generator.BasicValueGenerator;
import com.ktrueda.npegah.generator.FieldValueGenerator;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Fuzzy Object Generator.
 * <p>
 * Generating fuzzy object to detect NullPointerException-unsafe codes.
 */
public class FuzzyObjectGenerator {

    final private FieldValueGenerator fieldValueSelector;
    final private FieldFilter fieldFilter;

    public FuzzyObjectGenerator(FieldValueGenerator selector, FieldFilter filter) {
        this.fieldValueSelector = selector;
        this.fieldFilter = filter;

    }

    public FuzzyObjectGenerator() {
        this.fieldValueSelector = new BasicValueGenerator();
        this.fieldFilter = new AllFieldFilter();
    }

    @VisibleForTesting
    static HashSet<HashSet<Pair<Field, Object>>> cartesianProduct(Set<Pair<Field, Set<Object>>> args) {
        return args.stream().reduce(
            new HashSet<HashSet<Pair<Field, Object>>>() {{
                add(new HashSet());
            }},
            (HashSet<HashSet<Pair<Field, Object>>> acc, Pair<Field, Set<Object>> p) -> {
                return (HashSet<HashSet<Pair<Field, Object>>>) acc.stream().flatMap(a -> p.second.stream().map(
                    v -> {
                        HashSet<Pair<Field, Object>> result = new HashSet<>();
                        result.addAll(a);
                        result.add(new Pair(p.first, v));
                        return result;
                    }
                )).collect(Collectors.toSet());
            },
            (acc1, acc2) -> {
                acc1.addAll(acc2);
                return acc1;
            }
        );
    }

    /**
     * generating method.
     *
     * @param clz
     * @param <T>
     * @return
     */
    public <T> Set<T> generate(Class<T> clz) {
        Set<T> value = this.fieldValueSelector.value(clz);
        if (value != null) {
            return value;
        }

        final Set<Pair<Field, Set<Object>>> fieldCandidateMap = Arrays.stream(clz.getDeclaredFields())
            .filter(f -> this.fieldFilter.filter(clz, f))
            .map((Field f) -> (Pair<Field, Set<Object>>) new Pair(f, generate(f.getType())))
            .map(p -> {
                if (!p.second.contains(null)) {
                    p.second.add(null);
                }
                return p;
            })
            .collect(Collectors.toSet());

        return cartesianProduct(fieldCandidateMap).stream().map(fv -> {
            try {
                final T obj = clz.isArray() ? (T) Array.newInstance(clz.getComponentType(), 1) : clz.newInstance();
                fv.forEach(p -> {
                    p.first.setAccessible(true);
                    try {
                        p.first.set(obj, p.second);
                    } catch (IllegalAccessException e) {
                        throw new GahAssignFailedException();
                    }
                });
                return obj;
            } catch (InstantiationException e) {
                throw new GahInstantiationException();
            } catch (IllegalAccessException e) {
                throw new GahAssignFailedException();
            }
        }).collect(Collectors.toSet());
    }

}
