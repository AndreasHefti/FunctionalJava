package com.andreashefti.exercise;

import com.andreashefti.functional.Effect;
import org.junit.Test;

import java.nio.file.DirectoryStream;
import java.util.function.*;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

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
     *     by currying the functions. How to do that is described later in this exercise 1.4.
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

    /** 1.1 Most used functional interfaces of Java 8
     *
     *  {@link java.util.function.Function }:
     *      Defines a polymorphic one-arity function with one function-parameter of type T
     *      and a return value of type R.
     *
     *      Use the higher order function "compose" that is implemented as a default method to compose another
     *      Function before a given Function. The composed Function must have the same return type (or substitute of)
     *      as the given Function instances parameter type. This results in a new Function with parameter type of
     *      the composed Function and the the original return type
     *
     *      Function<V,R> = Function<T,R>.compose( Function<V,T> )
     *      Function<V,R> = Function<T,R>.compose( Function<? super V,? extends T> )
     *
     *      Use the higher order function "andThen" that is also implemented as default method to compose another
     *      Function after a given Function.
     *
     *      Function<T,V> = Function<T,R>.compose( Function<R,V> )
     *      Function<T,V> = Function<T,R>.compose( Function<? super R,? extends V> )
     *
     *      Use the static "identity" method to get an identity function that gives back what it receives.
     *
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
     *      This is mostly used to replace or abstract a imperative if statement.
     *
     *      Use the higher order function "and" to compose another Predicate with a given one by a logical and.
     *      Use the higher order function "or" to to compose another Predicate with a given one by a logical or.
     *      Use the function "negate" to get a new Predicate that represents a logical not of the original one.
     *
     *
     *  {@link java.util.function.Supplier }:
     *      A Supplier is a specialised Function that has no parameter and just a return value. It supplies something.
     *
     *
     *  {@link java.util.function.Consumer }:
     *      A Consumer is a specialised Function that has no return value and only one parameter. It consumes a value and
     *      dies something with it. This something mostly is an effect like logging, printing out, write to...
     *
     *      So if there is an effect that needs to be applied, it make sense to call it like this:
     *      {@link com.andreashefti.functional.Effect }
     *
     *  {@link java.lang.Runnable }:
     *      Runnable is not new and was mostly used within Thread(s). But as a functional interface, Runnable is just a
     *      Consumer or Effect without any parameter. Just a program that can be run.
     *
     *  NOTE: There a lot other functional interface definitions within the JDK's java.util.function package
     *        but most of them are specialisations of the above just dealing with primitive types or higher arity.
     *
     */
    @Test
    public void _FunctionExample() {

        // NOTE: this are still implementations with anonymous inner classes. Later we will use Lambda Expressions for that

        Function<String, Integer> length = new Function<String, Integer>() {
            @Override
            public Integer apply( String s ) {
                return ( s == null ) ? 0 : s.length();
            }
        };

        Function<Integer, String> toString = new Function<Integer, String>() {
            @Override
            public String apply( Integer i ) {
                return String.valueOf( i );
            }
        };

        assertTrue( 5 == length.apply( "Hello" ) );
        assertEquals( "55", toString.apply( 55 ) );

        // NOTE: in Java 8 we can use the method reference operator "::" to extract already existing functions to functional interfaces
        //       This is syntactical sugar from the Java 8 compiler and the function must match the functional interface declared
        //       on the left side:

        Function<String, Integer> lengthExtracted = String::length;
        Function<Integer, String> toStringExtracted = String::valueOf;

        assertTrue( 5 == lengthExtracted.apply( "Hello" ) );
        assertEquals( "55", toStringExtracted.apply( 55 ) );

        // * Use compose:

        Function<Integer, Integer> toStringLength = length.compose( toString );

        assertTrue( 4 == toStringLength.apply( 1000 ) );
        assertTrue( 6 == toStringLength.apply( 100000 ) );

        Function<String, String> lengthToString = toString.compose( length );

        assertEquals( "5", lengthToString.apply( "Hello" ) );

        // * Use andThen

        Function<String, String> lengthAndThenToString = length.andThen( toString );

        assertEquals( "5", lengthAndThenToString.apply( "Hello" ) );

        Function<Integer, Integer> toStringAndThenLength = toString.andThen( length );

        assertTrue( 4 == toStringAndThenLength.apply( 1000 ) );
        assertTrue( 6 == toStringAndThenLength.apply( 100000 ) );

        // * Use identity:

        Function<String, String> identity = Function.identity();

        assertEquals( "Hello", identity.apply( "Hello" ) );


    }

    @Test
    public void _BiFunctionExample() {

        // We want to abstract a simple addition of two Integer values within functional programming.
        // In this case we have two input parameter and one return value.
        // A Function has only one parameter so we can use BiFunction for that:

        BiFunction<Integer, Integer, Integer> addInt = new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply( Integer i1, Integer i2 ) {
                return i1 + i2;
            }
        };

        assertEquals( Integer.valueOf( 5 ), addInt.apply( 2, 3 ) );

    }

    @Test
    public void _PredicateExample() {
        Predicate<String> lengthOf5 = new Predicate<String>() {
            @Override
            public boolean test( String s ) {
                return ( s == null ) ? false : s.length() == 5;
            }
        };

        Predicate<String> startsWithA = new Predicate<String>() {
            @Override
            public boolean test( String s ) {
                return ( s == null ) ? false : s.startsWith( "A" );
            }
        };

        assertTrue( lengthOf5.test( "Hello" ) );
        assertFalse( lengthOf5.test( "Hello World" ) );
        assertTrue( startsWithA.test( "Anton" ) );
        assertFalse( startsWithA.test( "Hello" ) );

        // NOTE: We can also use method references here for methods that returns a boolean and has no additional parameter(s):
        Predicate<String> isEmptyExtracted = String::isEmpty;

        // * use and combination

        Predicate<String> lengthOf5ANDStartsWithA = lengthOf5.and( startsWithA );

        assertTrue( lengthOf5ANDStartsWithA.test( "Anton" ) );
        assertFalse( lengthOf5ANDStartsWithA.test( "Hello" ) );

        // * use or combination

        Predicate<String> lengthOf5ORStartsWithA = lengthOf5.or( startsWithA );

        assertTrue( lengthOf5ORStartsWithA.test( "Anton" ) );
        assertTrue( lengthOf5ORStartsWithA.test( "Hello" ) );
        assertFalse( lengthOf5ORStartsWithA.test( "Hello World" ) );

        // * use negate:

        Predicate<String> notLengthOf5 = lengthOf5.negate();

        assertTrue( notLengthOf5.test( "Hello World" ) );
        assertFalse( notLengthOf5.test( "Hello" ) );
    }

    @Test
    public void _SupplierExample() {

        Supplier<String> giveAHello = new Supplier<String>() {
            @Override
            public String get() {
                return "Hello";
            }
        };

        assertEquals( "Hello", giveAHello.get() );

        // NOTE: We can also use method references here for methods that has just a return type and no parameters
        //       It is possible even for an empty constructor

        Supplier<String> newString = String::new;

    }

    @Test
    public void _ConsumerAndEffectExample() {

        Consumer<String> println = new Consumer<String>() {
            @Override
            public void accept( String s ) {
                System.out.println( s );
            }
        };

        Effect<String> printlnAsEffect = new Effect<String>() {
            @Override
            public void apply( String s ) {
                System.out.println( s );
            }
        };

        println.accept( "Hello" );
        printlnAsEffect.apply( "Hello" );

        // NOTE: We can also use method references here for void methods that expects one parameter of specified type:

        Consumer<String> printlnExtracted = System.out::println;
        printlnExtracted.accept( "Hallo" );
    }

    @Test
    public void _RunnableExample() {

        oncePerSecond( () -> System.out.println( "second" ) );

    }

    static final void oncePerSecond( Runnable callback ) {
        int i = 0;
        while ( i < 10 ) {
            i++;
            callback.run();
            try {
                Thread.sleep( 1000 );
            } catch( InterruptedException e ) {
                e.printStackTrace();
            }
        }
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

        // we can do this within a Lambda expression and remove all the boilerplate code of anonymous inner class implementations
        // this is also more readable then the anonymous inner class implementation
        Function<String, Integer> length2 = s -> ( s == null )? 0 : s.length();

        assertTrue( 5 == length1.apply( "Hello" ) );
        assertTrue( 5 == length2.apply( "Hello" ) );


        // two- and (our self created) three-arity function can also be used within lambda expressions
        BiFunction<Integer, Integer, Integer> add = ( t, u ) -> t + u;
        TriFunction<String, String, String, String> concat = ( t, u, v ) -> t + u + v;

        assertTrue( 5 == add.apply( 2, 3 ) );
        assertEquals( "My Name Is: Steve", concat.apply( "My Name Is", ": ", "Steve" ) );

    }

    /** 1.3 Higher Order Functions and Function Composition
     *
     *
     *
     */

    /** 1.4 Closures
     *
     * TODO
     *
     */
    static Function<Integer, Integer> closureExample( int taxRate ) {
        return price -> price * taxRate;
    }



    /** 1.5 Function Currying and Partial Applying
     *
     */
    public void functionCurryingAndPartialApplying() {

        // In section 1.1 we used the BiFunction to create function to add two Integer values like this:

        BiFunction<Integer, Integer, Integer> addInt1 = new BiFunction<Integer, Integer, Integer>() {
            @Override
            public Integer apply( Integer i1, Integer i2 ) {
                return i1 + i2;
            }
        };

        // The same with Lambda Expression:
        BiFunction<Integer, Integer, Integer> addInt2 = (i1, i2) -> i1 + i2;

        assertEquals( Integer.valueOf( 5 ), addInt1.apply( 2, 3 ) );
        assertEquals( Integer.valueOf( 5 ), addInt2.apply( 2, 3 ) );

        // By currying functions we can also use two one parameter Functions to get to the same result:
        // NOTE: this implementation uses anonymous inner classes to show to simplification by using lambda expressions

        Function<Integer, Function<Integer, Integer>> add3 = new Function<Integer, Function<Integer, Integer>>() {

            @Override
            public Function<Integer, Integer> apply( Integer i1 ) {
                return new Function<Integer, Integer>() {
                    @Override
                    public Integer apply( Integer i2 ) {
                        return i1 + i2;
                    }
                };
            }
        };

        // The same with Lambda Expression:
        Function<Integer, Function<Integer, Integer>> add4 = i1 -> i2 -> i1 + i2;

        assertEquals( Integer.valueOf( 5 ), add3.apply( 2 ).apply( 3 ) );
        assertEquals( Integer.valueOf( 5 ), add4.apply( 3 ).apply( 2 ) );

        // With the curried version of the add function we can also use partial applying:

        Function<Integer, Integer> addTo100 = add3.apply( 100 );
        assertEquals( Integer.valueOf( 102 ), addTo100.apply( 2 ) );
        assertEquals( Integer.valueOf( 105 ), addTo100.apply( 5 ) );

    }

}
