package com.ktrueda.npegazz;

import java.util.Set;

public interface FieldValueSelector {
    <T> Set<T> value(Class<T> clz);
}
