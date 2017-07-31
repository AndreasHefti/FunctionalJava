package com.andreashefti.exercise;

import org.junit.Test;

import java.util.function.BiFunction;
import java.util.function.Function;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class _1_BasicFunctional {


    /** 1.0 Functional Interface
     *
     * A functional interface is a interface with only one abstract method.
     * A functional interface my have other methods with default implementations.
     * A functional interface should be marked with the @FunctionalInterface Annotation.
     * A functional interface can be used within a lambda expression.
     *
     * One famous example of an functional interface is java.util.function.Function that has an abstract method
     * R apply ( T t ) witch defines a function that receives a value of type T and gives back a value of type R.
     *
     * There is also a java.util.function.BiFunction that defines a two-arity function that receives two values as parameter.
     * Java goes no further with the arity of functions but we can defines three-arity function within a functional interface
     * ourselves as an example, the TriFunction.
     *
     * --> There is also another way to create more then single arity functions but within only the Function functional interface
     *     by currying the functions. How to do that is described later in this exercise.
     *
     */
    @FunctionalInterface
    interface TriFunction<T, U, V, R> {

        /** Defines a function with three parameters and one return type
         *
         * @param t parameter value of type T
         * @param u parameter value of type U
         * @param v parameter value of type V
         * @return value of type R
         */
        R apply ( T t, U u, V v );
    }

    /** 1.1.1 Most used functional interfaces of Java 8
     *
     *  {@link java.util.function.Function }:
     *      Defines a polymorphic one-arity function with one function-parameter of type T
     *      and a return value of type R.
     *
     *      Use compose to
     *
     *  {@link java.util.function.BiFunction }:
     *      Defines a polymorphic two-arity function with function-parameter of type T and U
     *      and a return value of type R.
     *
     *      There are no functional interfaces with more arity defined in the standard JDK but
     *      one can implement it like the TriFunction example above or within function currying
     *      described later in this examples.
     *
     *  {@link java.util.function.Predicate }:
     *      A specialisation of Function with a boolean as return type.
     *      This is mostly used to replace a imperative if statement. There is also
     *
     *
     *  {@link java.util.function.Supplier }:
     *      TODO
     *
     *  {@link java.util.function.Consumer }:
     *      TODO
     *      {@link com.andreashefti.functional.Effect }
     *
     *  NOTE: There a lot other functional interface definitions within the JDK's java.util.function package
     *        but most of them are specialisations of the above just dealing with primitive types or higher arity.
     *
     */
    public void mostUsedFunctionalInterfacesOfJava8() {
        // TODO
    }

    /** 1.2 Java 8. Lambda Expressions
     *
     * Lambda expressions are the new syntactical sugar of Java 8 and functional java to
     * create concrete implementations of functional interfaces.
     * Instead of writing the whole anonymous inner class of a functional interface one can
     * just use a Lambda expression.
     *
     */
    @Test
    public void lambdaExpressions() {

        // instead of doing this anonymous inner class implementations with a lot of writing and boilerplate code
        Function<String, Integer> length1 = new Function<String, Integer>() {
            @Override
            public Integer apply( String s ) {
                return ( s == null )? 0 : s.length();
            }
        };

        // we can do this within a Lambda expression and remove al the boilerplate code of anonymous inner class implementations
        // this is also more readable and understandable then the anonymous inner class implementation
        Function<String, Integer> length2 = s -> ( s == null )? 0 : s.length();

        assertTrue( 5 == length1.apply( "Hello" ) );
        assertTrue( 5 == length2.apply( "Hello" ) );


        // two- and (our self created) three-arity function can also be used within lambda expressions
        BiFunction<Integer, Integer, Integer> add = ( t, u ) -> t + u;
        TriFunction<String, String, String, String> concat = ( t, u, v ) -> t + u + v;

        assertTrue( 5 == add.apply( 2, 3 ) );
        assertEquals( "My Name Is: Steve", concat.apply( "My Name Is", ": ", "Steve" ) );
    }



    /** 1.3 Closures
     *
     * TODO
     *
     */
    static Function<Integer, Integer> closureExample( int taxRate ) {
        return price -> price * taxRate;
    }

    /** 1.4 Higher Order Functions and Function Composition
     *
     */

    /** 1.5 Function Currying and Partial Applying
     *
     */

}
