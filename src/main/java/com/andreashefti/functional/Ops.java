package com.andreashefti.functional;

import java.util.function.Function;

/**
 * Created by andreashefti on 04.09.17.
 */
public interface Ops {

    /** Defines an operation of two arguments as a curried function
     *
     *  Use this if you have a function of two arguments to implement
     *  Also partial applying is possible.
     *
     *  <code>
     *      Op2<Integer, Integer, Integer> add = x -> y -> x + y;
     *      Op2<Double, Integer, Double> addIntToDouble = x -> y -> x + y;
     *
     *      int seven = add.apply( 4 ).apply( 3 );
     *      double nine_five = addIntToDouble.apply( 4.5 ).apply( 5 );
     *
     *      // partial applying
     *      Function<Integer, Integer> addTo5 = add.apply( 5 );
     *      int seven = addTo5.apply( 2 );
     *  </code>
     *
     *
     * @param <T> The type of the first argument of the two argument-function
     * @param <U> The type of the second argument of the two argument-function
     * @param <R> The type of the result of the two argument-function
     */
    @FunctionalInterface
    interface Op2<T, U, R> extends Function<T, Function<U, R>>, Ops {}

    @FunctionalInterface
    interface Op3<T, U, V, R> extends Function<T, Function<U, Function<V, R>>> {}

    @FunctionalInterface
    interface Op4<T, U, V, W, R> extends Function<T, Function<U, Function<V, Function<W, R>>>> {}
}
