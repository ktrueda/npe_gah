package com.ktrueda.npegazz;

import org.junit.jupiter.api.Test;

public class Sample {
    public static class Foo {
        String field1;
        String field2;

        public Foo() {
        }

        @Override
        public String toString() {
            return "Foo{" +
                "field1='" + field1 + '\'' +
                ", field2='" + field2 + '\'' +
                '}';
        }

    }

    void targetMethod(Foo v) {
        v.field1.length();
        v.field2.length();
    }

    @Test
    void nullPointerDetector() {
        FuzzyObjectGenerator generatorByJava = new FuzzyObjectGenerator();
        for (Foo v : generatorByJava.generate(Foo.class)) {
            try {
                targetMethod(v);
            } catch (NullPointerException e) {
                System.out.println(v.toString());
            }
        }
    }

}
