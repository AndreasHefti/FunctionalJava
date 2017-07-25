package com.andreashefti.functional;

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
        public static <T> List<T > list( final T value ) {
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
        public static <T> List<T > list( final T... values ) {
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
        public static <T> List<T > list( final List<T> list ) {
            return Collections.unmodifiableList( copy( list ) );
        }

        /** Use this to get the first element in a given list.
         *
         * @param list The list to get the first element from
         * @param <T> the type of the List and the element
         * @return the element
         * @throws IllegalStateException If the given list is empty or null
         */
        public static <T> T head( final List<T> list ) {
            if ( list == null || list.size() == 0 ) {
                throw new IllegalStateException( "head of empty list" );
            }
            return list.get( 0 );
        }

        /** Use this to copy a given list to a new mutable List of the same type.
         *
         * @param list The list to copy
         * @param <T> The type of the list
         * @return New immutable List as a copy of the given List.
         */
        public static <T> List<T > copy( final List<T> list ) {
            return new ArrayList<>( list );
        }

        /** Use this to get the tail of a given List
         *  This gives a new immutable list with all tail elements (without the first element) of a given List.
         *
         * @param list The List to get the tail-List from
         * @param <T> the type of the List
         * @return a new immutable list with all tail elements (without the first element)
         * @throws IllegalStateException If the given List is null or empty.
         */
        public static <T> List<T> tail( final List<T> list ) {
            if ( list == null || list.size() == 0 ) {
                throw new IllegalStateException( "tail of empty list" );
            }

            List<T> workList = new ArrayList<>( list );
            workList.remove(0);
            return Collections.unmodifiableList( workList );
        }

        /** Use this to apply an effect on each element of a given list.
         *
         * @param list The list to apply an effect on each element
         * @param effect The effect
         * @param <T> The type of the list and effect
         */
        public static <T> void applyEffect( final List<T> list, final Effect<T> effect ) {
            for ( T t : list ) {
                effect.apply( t );
            }
        }

        /** This is list map operation that maps each element from a given list to an element in a new created list within a given function.
         *
         * @param list The source list
         * @param f The map function
         * @param <T> The source type
         * @param <U> The target type
         * @return a new unmodifiable List of target typed elements
         */
        public static <T, U> List<U> map( final List<T> list, final Function<T, U> f ) {
            return mapImperative( list, f );
        }

        /** This is a imperative implementation of list map operation that maps
         *  each element from a given list to an element in a new created list within a given function.
         *
         * @param list The source list
         * @param f The map function
         * @param <T> The source type
         * @param <U> The target type
         * @return a new unmodifiable List of target typed elements
         */
        static <T, U> List<U> mapImperative( final List<T> list, final Function<T, U> f ) {
            List<U> newList = new ArrayList<>();

            for ( T value : list ) {
                newList.add( f.apply( value ) );
            }

            return Collections.unmodifiableList( newList );
        }

        /** This is a implementation of list map operation that uses foldLeft operation
         *
         *  consider that map consists of two operations: applying a function to each element, and then gathering all elements into a new list.
         *  This second operation is a fold, where the identity is the empty list
         *
         *
         * @param list The source list
         * @param f The map function
         * @param <T> The source type
         * @param <U> The target type
         * @return a new unmodifiable List of target typed elements
         */
        static <T, U> List<U> mapFoldLeft( final List<T> list, final Function<T, U> f ) {
            return leftFold( list, list(), x -> y -> append( x, f.apply( y ) ) );
        }

        /** This is a implementation of list map operation that uses foldRight operation
         *
         *  consider that map consists of two operations: applying a function to each element, and then gathering all elements into a new list.
         *  This second operation is a fold, where the identity is the empty list
         *
         *
         * @param list The source list
         * @param f The map function
         * @param <T> The source type
         * @param <U> The target type
         * @return a new unmodifiable List of target typed elements
         */
        static <T, U> List<U> mapFoldRight( final List<T> list, final Function<T, U> f ) {
            return foldRight( list, list(), x -> y -> prepend( y, f.apply( x ) ) );
        }


        /* Following some different implementations for List append, prepend and reverse operations */

        /** Use this to functionally append an element to a List.
         *  This creates a new immutable List from a given List with the given value appended.
         *
         * @param list The List to create a new list from and append the value
         * @param value The value to append to the new List
         * @param <T> the type of the List
         * @return a new immutable List from the given List with the given value appended.
         */
        public static <T> List<T> append( final List<T> list, final T value ) {
            List<T> ts = copy(list);
            ts.add( value );
            return Collections.unmodifiableList( ts );
        }

        /** A prepend operation for Lists. copies the given list
         *  to a new mutable one, used the add method of the list to add the given value on position 0
         *  and return a new immutable list instance of the new list
         *
         *  Use this to prepend a value to a List with not mutating the original list
         *
         * @param list The list to create a copy and prepend the given value
         * @param value The value to prepend
         * @param <T> The type of the List
         * @return a new immutable list instance with the given value prepended to the given list
         */
        public static <T> List<T> prepend( final List<T> list, final T value ) {
            return prependImperative( list, value );
        }

        /** A simple imperative prepend operation for List copies the given list
         *  to a new mutable one, used the add method of the list to add the given value on position 0
         *  and return a new immutable list instance of the new list
         *
         * @param list The list to create a copy and prepend the given value
         * @param value The value to prepend
         * @param <T> The type of the List
         * @return a new immutable list instance with the given value prepended to the given list
         */
        static <T> List<T> prependImperative( final List<T> list, final T value ) {
            List<T> ts = copy(list);
            ts.add( 0, value );
            return Collections.unmodifiableList( ts );
        }

        /** A prepend operation using foldLeft.
         *  This uses foldLeftImperative method with the given list and a list with the value as single element as
         *  the identity and a function that uses the append method to append each value from the list to the left folding result
         *
         *  <pre>foldLeftImperative( list, list( value ), a -> b -> append( a, b ) );</pre>
         *
         *  NOTE: Do not use this in production code because this traversing the list several times and has bad performance
         *        This just demonstrates a possible but naive implementation in a functional way.
         *        More functional implementations with better performance will follow.
         *
         * @param list The list to create a copy and prepend the given value
         * @param value The value to prepend
         * @param <T> The type of the List
         * @return a new immutable list instance with the given value prepended to the given list
         */
        static <T> List<T> prependFoldLeft( final List<T> list, final T value ) {
            return leftFold( list, list( value ), a -> b -> append( a, b ) );
        }

        public static List<Integer> range( Integer start, Integer end ) {
            return rangeRecursiveStackSave_( list(), start, end ).invoke();
        }

        /** A naive implementation of rage using none tail call recursive approach */
        public static List<Integer> rangeRecursive_( Integer start, Integer end ) {
            return end <= start?
                       list() :
                       prepend( rangeRecursive_( start + 1, end ), start );
        }

        /** A tail call recursive implementation of rage */
        public static List<Integer> rangeRecursiveTailCall_( List<Integer> acc, Integer start, Integer end ) {
            return end <= start?
                       acc :
                       rangeRecursiveTailCall_( append( acc, start ), start + 1, end );
        }

        /** A tail call recursive and stack save implementation of rage using Trampoline */
        public static Trampoline<List<Integer>> rangeRecursiveStackSave_( List<Integer> acc, Integer start, Integer end ) {
            return end <= start?
                       Trampoline.done( acc ) :
                       Trampoline.call( () -> rangeRecursiveStackSave_( append( acc, start ), start + 1, end ) );
        }

        /** This is list reverse operation.
         *  Use this to create a new reversed List of the same type from a given List
         *
         * @param list The list to get an reversed List from
         * @param <T> The type of the list
         * @return new reversed List
         */
        public static <T> List<T> reverse( List<T> list ) {
            return reverseImperative( list );

        }

        /** This is a imperative implementation of list reverse operation.
         *  Use this to create a new reversed List of the same type from a given List
         *
         * @param list The list to get an reversed List from
         * @param <T> The type of the list
         * @return new reversed List
         */
        static <T> List<T> reverseImperative( final List<T> list ) {
            List<T> result = new ArrayList<>();
            for( int i = list.size() - 1; i >= 0; i-- ) {
                result.add( list.get( i ) );
            }
            return Collections.unmodifiableList( result );
        }

        /** This is a more functional implementation of list reverse operation.
         *  This is using the foldLeftImperative method with the list an empty list as identity and the prepend method as the function
         *
         *  NOTE: Do not use this in production code because this traversing the list several times and has bad performance
         *        This just demonstrates a possible but naive implementation in a functional way
         *        More functional implementations with better performance will follow.
         *
         * @param list The list to get an reversed List from
         * @param <T> The type of the list
         * @return new reversed List
         */
        static <T> List<T> reversePrepend( final List<T> list ) {
            return leftFold( list, list(), x -> y -> prepend( x, y ) );
        }

        /** This is the same like reversePrepend but instead of using prepend method it uses the foldLeftImperative from prepend
         *  directly.
         *
         *  NOTE: Do not use this in production code because this traversing the list several times and has bad performance
         *        This just demonstrates a possible but naive implementation in a functional way
         *        More functional implementations with better performance will follow.
         *
         * @param list The list to get an reversed List from
         * @param <T> The type of the list
         * @return new reversed List
         */
        static <T> List<T> reverseFoldLeft( final List<T> list ) {
            return leftFold( list, list(), x -> y -> leftFold( x, list( y ), ( List<T> a ) -> ( T b ) -> append( a, b ) ) );
        }



        /* NOTE: Following some different implementation for left- and right-fold operations for List's
         *       There are imperative and recursive implementations for this operations and the advantages and
         *       disadvantages for each implementation is described within the JavaDoc.
         */

        /** This is a left fold operation for a given list with a given identity using a given function.
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        public static <T, U> U leftFold( final List<T> list, final U identity, final Function<U, Function<T, U>> f ) {
            return foldLeftRecursiveStackSave_( list, identity, f ).invoke();
        }

        /** This is a imperative implementation of a left fold of a given list with a given entity using
         *  a given function.
         *
         *  Fold left is a corecursive operation so implementing it through an imperative loop is the easiest way
         *
         *  NOTE: This can be used in production code with no limitation.
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        static <T, U> U foldLeftImperative( final List<T> list, final U identity, final Function<U, Function<T, U>> f ) {
            U result = identity;
            for ( T t : list ) {
                result = f.apply( result ).apply( t );
            }
            return result;
        }

        /** This is a recursive implementation of a left fold of a given list with a given entity using
         *  a given function.
         *
         *  Fold left is a corecursive operation but since every corecursive operation can also implemented in a recursive way,
         *  this is an example how to do that.
         *
         *  NOTE: This should not be used in production code because there are two mayor disadvantages within this;
         *        1. Because of using tail function and creating a new list for every step it has bad performance
         *        2. Since recursive calls (even tail recursive calls) in Java are Stack based they have a limit
         *           (The limit of the memory for the Stack within the running JVM)
         *
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        static <T, U> U foldLeftRecursive( final List<T> list, final U identity, final Function<U, Function<T, U>> f ) {
            return list.isEmpty()?
                       identity :
                       foldLeftRecursive( tail( list ), f.apply( identity ).apply( head( list ) ), f );
        }

        /** This is a fully stack save recursive implementation of left fold using the Trampoline.
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        static <T, U> Trampoline<U> foldLeftRecursiveStackSave_( final List<T> list, final U identity, final Function<U, Function<T, U>> f ) {
            return list.isEmpty()?
                       Trampoline.done( identity ) :
                       Trampoline.call( () -> foldLeftRecursiveStackSave_( tail( list ), f.apply( identity ).apply( head( list ) ), f ) );
        }

        /** This is a right fold operation for a given list with a given identity using a given function.
         *
         *  Fold right is a recursive operation so implementing it through an imperative loop we have to process the list on reverse order
         *
         *  NOTE: This can be used in production code with no limitation.
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        public static <T, U> U foldRight( final List<T> list, final U identity, final Function<T, Function<U, U>> f ) {
            return foldRightImperative( list, identity, f );
        }

        /** This is a imperative implementation of a right fold of a given list with a given identity using
         *  a given function.
         *
         *  Fold right is a recursive operation so implementing it through an imperative loop we have to process the list on reverse order
         *
         *  NOTE: This can be used in production code with no limitation.
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        static <T, U> U foldRightImperative( final List<T> list, final U identity, final Function<T, Function<U, U>> f ) {
            U result = identity;
            for ( int i = list.size() - 1; i >= 0; i-- ) {
                result = f.apply( list.get( i ) ).apply( result );
            }
            return result;
        }

        /** This is a recursive implementation of a right fold of a given list with a given entity using
         *  a given function.
         *
         *  Fold right is a recursive operation so doing it through recursive method call is the natural way of implementation
         *
         *  NOTE: This should not be used in production code because there are two mayor disadvantages within this;
         *        1. Because of using tail function and creating a new list for every step it has bad performance
         *        2. Since recursive calls (even tail recursive calls) in Java are Stack based they have a limit
         *           (The limit of the memory for the Stack within the running JVM)
         *
         * @param list The List to fold from left side
         * @param identity An identity value that acts as a start value
         * @param f The function that is applied for each element in the list
         * @param <T> The type of the List
         * @param <U> The type of the result (and the identity)
         * @return The result of the left side list folding with given identity and function
         */
        static <T, U> U foldRightRecursive( final List<T> list, final U identity, final Function<T, Function<U, U>> f ) {
            return list.isEmpty()?
                       identity :
                       f.apply( head( list ) ).apply( foldRightRecursive( tail( list ), identity, f) );
        }

        static <T, U> U foldRightRecursiveTailCall_( final U acc, final List<T> list, final U identity, final Function<U, Function<T, U>> f ) {
            return list.isEmpty()?
                       acc :
                       foldRightRecursiveTailCall_( f.apply( acc ).apply( head( list ) ), tail( list ), identity, f );
        }

//        static <T, U> Trampoline<U> foldRightRecursiveStackSave_( final List<T> list, final U identity, final Function<T, Function<U, U>> f ) {
//            return list.isEmpty()?
//                       identity:
//                       Trampoline.call( () -> f.apply( head( list ) ).apply( foldRightRecursiveStackSave_( tail( list ), identity, f ) ) );
//        }

    }

}
