package com.ktrueda.npegah;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ktrueda.npegah.exception.GahAssignFailedException;
import com.ktrueda.npegah.exception.GahInstantiationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

class FuzzyObjectGeneratorTest {


    @Test
    void cartesianProduct() throws Exception {
        Field f1 = Foo.class.getDeclaredField("f1");
        Field f2 = Foo.class.getDeclaredField("f2");

        assertEquals(
            new HashSet<HashSet<Pair<Field, Object>>>() {{
                add(new HashSet<Pair<Field, Object>>() {{
                    add(new Pair<>(f1, "random-string"));
                    add(new Pair<>(f2, 123));
                }});
                add(new HashSet<Pair<Field, Object>>() {{
                    add(new Pair<>(f1, "random-string"));
                    add(new Pair<>(f2, null));
                }});
                add(new HashSet<Pair<Field, Object>>() {{
                    add(new Pair<>(f1, null));
                    add(new Pair<>(f2, 123));
                }});
                add(new HashSet<Pair<Field, Object>>() {{
                    add(new Pair<>(f1, null));
                    add(new Pair<>(f2, null));
                }});
            }},
            FuzzyObjectGenerator.cartesianProduct(new HashSet<Pair<Field, Set<Object>>>() {{
                add(new Pair(f1, new HashSet<String>() {{
                    add("random-string");
                    add(null);
                }}));
                add(new Pair(f2, new HashSet<Integer>() {{
                    add(123);
                    add(null);
                }}));
            }})
        );

    }

    @Nested
    class generate {

        private FuzzyObjectGenerator sut = new FuzzyObjectGenerator();

        @Nested
        class userDefinedClass {
            @Test
            void nested() throws Exception {
                assertEquals(
                    // (#f1) * (#f2) * (#b) * (#fss)
                    2 * 2 * 3 * 3,
                    sut.generate(Foo.class).size()
                );
            }
        }

        @TestInstance(TestInstance.Lifecycle.PER_CLASS)
        @Nested
        class itemAndArray {
            @ParameterizedTest
            @MethodSource("eachClassMethodSource")
            void eachClass(Class clz, HashSet expected) {
                if (clz.isArray()) {
                    Set actual = sut.generate(clz);
                    if (actual == null) {
                        assertNull(expected);
                    } else {
                        assertTrue(expected.stream().allMatch(e1 -> actual.stream().anyMatch(e2 -> Arrays.equals((Object[]) e1, (Object[]) e2))));
                        assertTrue(actual.stream().allMatch(e1 -> expected.stream().anyMatch(e2 -> Arrays.equals((Object[]) e1, (Object[]) e2))));
                    }
                } else {
                    assertEquals(
                        expected,
                        sut.generate(clz)
                    );
                }
            }

            Stream<Arguments> eachClassMethodSource() {
                return Stream.of(
                    Arguments.arguments(
                        String.class,
                        new HashSet<String>() {{
                            add("string-value");
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        String[].class,
                        new HashSet<String[]>() {{
                            add(new String[]{"string-value"});
                            add(new String[]{});
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Integer.class,
                        new HashSet<Integer>() {{
                            add(123);
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Integer[].class,
                        new HashSet<Integer[]>() {{
                            add(new Integer[]{123});
                            add(new Integer[]{});
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Float.class,
                        new HashSet<Float>() {{
                            add(3.14f);
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Float[].class,
                        new HashSet<Float[]>() {{
                            add(new Float[]{3.14f});
                            add(new Float[]{});
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Double.class,
                        new HashSet<Double>() {{
                            add(2.718d);
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Double[].class,
                        new HashSet<Double[]>() {{
                            add(new Double[]{2.718d});
                            add(new Double[]{});
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Boolean.class,
                        new HashSet<Boolean>() {{
                            add(true);
                            add(null);
                        }}
                    ),
                    Arguments.arguments(
                        Boolean[].class,
                        new HashSet<Boolean[]>() {{
                            add(new Boolean[]{true});
                            add(new Boolean[]{});
                            add(null);
                        }}
                    )
                );
            }
        }
    }

    @Nested
    class invalidConstructor {
        private FuzzyObjectGenerator sut = new FuzzyObjectGenerator();

        @Test
        void instantiation() throws Exception {
            try {
                sut.generate(InvalidTypeConstructor.class);
                fail();
            } catch (GahInstantiationException e) {
            }
        }

        @Test
        void assign() throws Exception {
            try {
                sut.generate(OnlyFinalStatic.class);
                fail();
            } catch (GahAssignFailedException e) {

            }

        }
    }

    static class Bar {
        @JsonProperty
        Float f3;

        @Override
        public String toString() {
            return "Bar{" +
                "f3=" + f3 +
                '}';
        }
    }

    static class Foo {
        @JsonProperty
        String f1;
        @JsonProperty
        Integer f2;
        @JsonProperty
        Bar b;
        @JsonProperty
        String[] fss;

        @Override
        public String toString() {
            return "Foo{" +
                "f1='" + f1 + '\'' +
                ", f2=" + f2 +
                ", b=" + b +
                ", fss='" + fss + '\'' +
                '}';
        }
    }

    static class InvalidTypeConstructor {
        InvalidTypeConstructor(int i) {
        }

        String s;
    }

    static class OnlyFinalStatic {
        private static final String s = "";
    }
}