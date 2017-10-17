package com.andreashefti.functional;

import java.util.function.Function;

/** A functional interface that represents an effect, function with one argument and returns no results.
 *  This is the same as java.util.function.Consumer but with a more functional terminology
 *
 *  <p>This is a <a href="package-summary.html">functional interface</a>
 *  whose functional method is {@link #apply(Object)}.
 *
 * @param <T> the type of the input to the operation
 */
@FunctionalInterface
public interface Effect<T> {

    void apply( T t );

    default Effect<T> andThen( Effect<T> anotherEffect ) {
        return t -> { apply( t ); anotherEffect.apply( t ); };
    }

    default Effect<T> andThen( Runnable runnable ) {
        return t -> { apply( t ); runnable.run(); };
    }

    static <T> Function<Effect<T>, Function<Effect<T>, Effect<T>>> compose() {
        return x -> y -> t -> {
            x.apply( t );
            y.apply( t );
        };
    }
}
