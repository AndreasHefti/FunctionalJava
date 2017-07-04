package com.andreashefti.exercise.functional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
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
     *  can be used like a constant to compose functions ans also partial apply the functions to compose.
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
        return f -> g -> g.compose( f );
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


    /** Contains functional utilities for collections (mostly Lists) */
    public static abstract class Collection {

        /** Use this to get an empty List of specified type.
         * The List is immutable
         *
         * @param <T> The type of the List
         * @return An empty immutable List
         */
        public static <T> List<T> list() {
            return Collections.emptyList();
        }

        /** Use this to create a List of specified type with the given argument as content.
         * The List is immutable
         *
         * @param value the first and only value in the list.
         * @param <T> The type of the List
         * @return An immutable list with the given argument as first and only value
         */
        public static <T> List<T > list( T value ) {
            return Collections.singletonList( value );
        }

        /** Use this to create a List of specified type with the given argument as content.
         * The List is immutable
         *
         * @param values Values of new List
         * @param <T> The type of the List
         * @return An immutable list with the given argument as value(s)
         */
        @SafeVarargs
        public static <T> List<T > list( T... values ) {
            return Collections.unmodifiableList(
                Arrays.asList( Arrays.copyOf( values, values.length ) )
            );
        }

        /** Use this to create a new immutable list of specified type from a given existing list of the same type.
         * This calls and does the same as copy
         * @param list The source List
         * @param <T> The type of the list
         * @return a new immutable list of specified type
         */
        public static <T> List<T > list( List<T> list ) {
            return copy( list );
        }

        /** Use this to get the first element in a given list.
         *
         * @param list The list to get the first element from
         * @param <T> the type of the List and the element
         * @return the element
         * @throws IllegalStateException If the given list is empty or null
         */
        public static <T> T head( List<T> list ) {
            if ( list == null || list.size() == 0 ) {
                throw new IllegalStateException( "head of empty list" );
            }
            return list.get( 0 );
        }

        /** Use this to copy a given list to a new immutable List of the same type.
         *
         * @param list The list to copy
         * @param <T> The type of the list
         * @return New immutable List as a copy of the given List.
         */
        public static <T> List<T > copy( List<T> list ) {
            return Collections.unmodifiableList( new ArrayList<>( list ) );
        }

        /** Use this to get the tail of a given List
         *  This gives a new immutable list with all tail elements (without the first element) of a given List.
         *
         * @param list The List to get the tail-List from
         * @param <T> the type of the List
         * @return a new immutable list with all tail elements (without the first element)
         * @throws IllegalStateException If the given List is null or empty.
         */
        public static <T> List<T> tail( List<T> list ) {
            if ( list == null || list.size() == 0 ) {
                throw new IllegalStateException( "tail of empty list" );
            }

            List<T> workList = new ArrayList<>( list );
            workList.remove(0);
            return Collections.unmodifiableList( workList );
        }

        /** Use this to functionally append an element to a List.
         *  This creates a new immutable List from a given List with the given value appended.
         *
         * @param list The List to create a new list from and append the value
         * @param value The value to append to the new List
         * @param <T> the type of the List
         * @return a new immutable List from the given List with the given value appended.
         */
        public static <T> List<T> append( List<T> list, T value ) {
            List<T> ts = copy(list);
            ts.add( value );
            return Collections.unmodifiableList( ts );
        }

        /** Use this to fold a given list from the left side with a given identity (start value) and within
         *  a given function that is applied to each element in the list.
         *
         *  This is abstracting normal/imperative list iteration in a functional manner that can be used within
         *  functional programming.
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        public static <T, U> U foldLeft( List<T> list, U identity, Function<U, Function<T, U>> f ) {
            U result = identity;
            for ( T t : list ) {
                result = f.apply( result ).apply( t );
            }
            return result;
        }

    }

}
