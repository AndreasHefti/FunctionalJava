package com.andreashefti.exercise.functional;

import java.util.function.Function;


public abstract class FunctionUtils {

    /** A higher order function to compose two functions as a constant reference
     *
     * @param f the first function
     * @param g the second function
     * @param <T> the input type of the first function and output type of the second function
     * @param <R> the output type
     * @param <V> the input type of the second function
     * @return a new function that is the composition of f with g, that has the input type V and output type R
     */
    public static final <T, R, V> Function<V, R> compose( Function<T, R> f, Function <V, T> g ) {
        return x -> f.apply( g.apply( x ) );
    }

    /** A higher order function to compose two functions that can be seen and used as a constant.
     *  This also allows currying or partial applying of the first function.
     *  See FunctionCompositionTests:curryingWithComposeAndHigherComposeConstants
     *
     * @param <T>
     * @param <R>
     * @param <V>
     * @return
     */
    public static final <T, R, V> Function<Function<T, R>,
                              Function<Function<R, V>,
                                       Function<T, V>>> compose() {
        return x -> y -> y.compose( x );
    }

    public static final <T, R, V> Function<Function<R, V>,
                              Function<Function<T, R>,
                                       Function<T, V>>> higherCompose() {
        return x -> y -> z -> x.apply( y.apply( z ) );
    }

    public static final <T, U, V> Function<Function<T, U>,
                              Function<Function<V, T>,
                                       Function<V, U>>> andThen() {
        return x -> y -> y.andThen( x );
    }
}
