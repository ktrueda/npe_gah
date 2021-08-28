# NullPointerException Gazz

All Java developers dislike NullPointerException(NPE). To avoid NPE we should use Optional type or annotations
like `@Nullable`/`@Notnull`. But they are optional and sometimes it's wrong(null was assigned to `@Notnull` field).
These days null-safe language like Scala/Kotlin is a good solution for this problem. However, we need to write Java for
some reasons.

NPE Gazz is a fuzz test tool to avoid NPE. You can find null-unsafe codes during unit testing.

```java
public class Main {
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
        FuzzyObjectGenerator generator = new FuzzyObjectGenerator();
        for (Foo v : generator.generate(Foo.class)) {
            try {
                targetMethod(v);
            } catch (NullPointerException e) {
                System.out.println(v.toString());
            }
        }
    }
}
```

Output is below. You can see some arguments which cause NPE.

```bash
Foo{field1='null', field2='string-value'}
Foo{field1='null', field2='null'}
Foo{field1='string-value', field2='null'}
```




