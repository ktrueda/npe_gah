package com.ktrueda.npegah;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ktrueda.npegah.fieldfilter.JsonPropertyFilter;
import com.ktrueda.npegah.generator.BasicValueGenerator;
import org.junit.jupiter.api.Test;

public class JsonPropertySample {
    public static class Foo {
        @JsonProperty
        String field1;
        @JsonProperty
        String field2;
        String field3;

        public Foo() {
        }

        @Override
        public String toString() {
            return "Foo{" +
                "field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                ", field3='" + field3 + '\'' +
                '}';
        }

    }

    void targetMethod(Foo v) {
        v.field1.length();
        v.field2.length();
    }

    @Test
    void nullPointerDetector() {
        FuzzyObjectGenerator generatorByJava = new FuzzyObjectGenerator(
            new BasicValueGenerator(),
            new JsonPropertyFilter()
        );
        for (Foo v : generatorByJava.generate(Foo.class)) {
            try {
                targetMethod(v);
            } catch (NullPointerException e) {
                System.out.println(v.toString());
            }
        }
    }

}
