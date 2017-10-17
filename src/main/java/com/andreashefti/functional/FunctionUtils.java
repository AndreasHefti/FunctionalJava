package com.andreashefti.functional;

import java.util.function.Function;


public abstract class FunctionUtils {



    /** A higher order function to compose two functions implemented as a method
     *
     * @param f the first function
     * @param g the second function
     * @param <T> the input type of the first function and output type of the second function
     * @param <U> the output type
     * @param <V> the input type of the second function
     * @return a new function that is the composition of f with g, that has the input type V and output type U
     */
    public static final <T, U, V> Function<V, U> compose( Function<T, U> f, Function <V, T> g ) {
        return x -> f.apply( g.apply( x ) );
    }

    /** A higher order function to compose two functions. This is implemented itself as a function an
     *  can be used like a constant to compose functions and also partial apply the functions to compose.
     *  <p>
     *  See FunctionCompositionTests:curryingWithComposeAndHigherComposeConstants
     *  <p>
     *  <pre>
     *      // given:
     *      Function<Long, Double> f = a ->  a + 2.0;
     *      Function<Double, Integer> g = a -> (int) ( a * 3 );
     *
     *      // currying the first parameter/function --> f
     *      Function<Function<Double, Integer>, Function<Long, Integer>> fApplied = FunctionUtils.<Long, Double, Integer>compose().apply( f );
     *
     *      // currying the second parameter/function --> g
     *      Function<Long, Integer> h = fApplied.apply( g );
     *
     *      // Result:
     *      h.apply( 1L ) --> 9
     *  </pre>
     *
     * @param <T>
     * @param <U>
     * @param <V>
     * @return
     */
    public static final <T, U, V> Function<Function<T, U>,
                                           Function<Function<U, V>,
                                                    Function<T, V>>> compose() {
        return f -> g -> x -> g.apply( f.apply( x ) );  // f -> g -> g.compose( f );
    }

    public static final <T, U, V> Function<Function<U, V>,
                                           Function<Function<T, U>,
                                                    Function<T, V>>> higherCompose() {
        return f -> g -> x -> f.apply( g.apply( x ) );
    }

    public static final <T, U, V> Function<Function<T, U>,
                                           Function<Function<V, T>,
                                                    Function<V, U>>> andThen() {
        return f -> g -> g.andThen( f );
    }

    public static final <T, U, V> Function<Function<T, U>,
                                           Function<Function<U, V>,
                                                    Function<T, V>>> higherAndThen() {
        return f -> g -> x -> g.apply( f.apply( x ) );
    }

}
