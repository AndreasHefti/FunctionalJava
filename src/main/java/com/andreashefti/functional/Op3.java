package com.andreashefti.functional;

import java.util.function.Function;

/**
 * Created by andreashefti on 13.06.17.
 */
public interface Op3<T, U, V, R> extends Function<T, Function<U, Function<V, R>>> {
}
