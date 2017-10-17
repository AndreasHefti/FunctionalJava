package com.andreashefti.exercise;

import com.andreashefti.functional.Effect;
import com.andreashefti.functional.Result;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.assertEquals;

/**
 * Created by andreashefti on 31.07.17.
 */
public class _2_Separation_And_Abstraction {

    /** 2 Separation and Abstraction
     *
     * Functional programming is all about separation of data-processing and effects of a program
     * and abstracting imperative control structures by composing functions instead of results of
     * functions or values like in an imperative style.<p>
     * A pure function has no side-effect(s) and separates the data-processing part of a program
     * from the effect(s) - which generally are the main
     * purpose of a program - in a functional style.
     *
     */


    /** 2.1 Separation of data-processing and effect
     *
     *  A pure function never has a side-effect.<br>
     *  Mutating state or references or reading or writing from/to an output is called an effect and can be
     *  defined (in a functional way) as function with one or more arguments and no return value.<br>
     *  This is not a pure function but in Java we can define such an effect within a Functional Interface
     *  what in fact is already done with the Consumer interface. Consumer is a more general name and it would be better
     *  having a more precise name when applying an effect. So we can just define a Functional Interface called Effect
     *
     *  It is also possible to compose Effect's with andThen to create a new Effect that applies the original Effect
     *  first and then the composed one.
     *  There is also a andThen for a Runnable which defines an Effect with no input value or just a runnable program.
     */
    @Test
    public void effect() {
        Runnable pln = () -> System.out.println();

        Effect<String> e1 = System.out::println;
        Effect<String> e2 = s -> System.out.println( "Hello " + s );
        Effect<String> e3 = s -> System.out.println( "Bye " + s );

        Effect<String> e11 = e1.andThen( pln );
        Effect<String> e21 = e2.andThen( pln );
        Effect<String> e31 = e3.andThen( pln );

        Effect<String> e12 = e11.andThen( e21 );
        Effect<String> e123 = e12.andThen( e31 );

        e123.apply( "World" );
    }

    /** Another problem in functional programming is the absence of data and/or an error case which
     *  normally causes absence of data and also an effect.<br>
     *  How is the absence of data normally handled in imperative programming? mostly the absence of data
     *  is indicated by the a null-reference, sometimes by a specific void-value like Double.NaN. And the
     *  imperative code checks against null-reference or void value every time the developer, that writes the
     *  code, has the idea that a null-reference check is needed otherwise a NullPointerException is thrown.<p>
     *  The functional programming way to solve this problem is to always give data even on a error case or if
     *  a computation is not able to produce date for a specific set of given parameter.<br>
     *  The Java answer for the absence of data problem is the Optional type that can be returned within a function
     *  or method and stores the computed value or indicates that the value is absent. The problem with the Optional
     *  is that it has no indicator about the reason of the absence of data. So there is no error-handling
     *  possible with the Optional type from Java.<br>
     *  We need something like the Optional type but with the possibility to also hold or indicate an error case.
     *  Therefore we introduce the Result type
     *
     *  Consider we have a map of names mapped to double values. Since the Java Map get method returns null if a
     *  key is not present in the map we can define a composed function "valueForName" where we can first apply
     *  the Java Map and then a key to get the mapped value within a Result Success or an Empty Result if there is no
     *  mapping for the specified key in the map.
     *
     *  And we have another composed function that divides two double values and gives a Result Success or a Result
     *  Failure in case of 0 divisor or if the given value or the given divisor is not a number, an Empty Result.
     *
     *  Now We Define a Map with some values and a composed "function" that first calls the "valueForName" function
     *  to get the value form the name map within a Result and then applies the value of the Result to the "divide"
     *  function or Double.NaN if the Result was not a Success result. This is accomplished with the getOrElse method
     *  of the Result.
     *
     *  Now we can use this "function" without any worry, we always get a Result
     *
     */
    @Test
    public void result() {
        Function<Map<String, Double>, Function<String, Result<Double>>> valueForName =
            map -> key ->
                map.containsKey( key )?
                    Result.success( map.get( key ) ):
                    Result.empty();

        Function<Double, Function<Double, Result<Double>>> divide =
            value -> divisor ->
                ( divisor == 0.0 )?
                    Result.failure( "divisor is 0" ):
                    ( Double.isNaN( divisor ) || Double.isNaN( value ) )?
                        Result.empty():
                        Result.success( value / divisor );


        Map<String, Double> values = new HashMap<>();
        values.put( "Simon", 5.0 );
        values.put( "Ada", 6.6 );
        values.put( "Pascal", 0.0 );

        Function<String, Function<Double, Result<Double>>> function =
            s -> v ->
                 divide
                    .apply( v )
                    .apply(
                        valueForName
                            .apply( values )
                            .apply( s )
                            .getOrElse( Double.NaN )
                    );

        assertEquals(
            "Success{value=2.0}",
            function.apply( "Simon" ).apply( 10.0 ).toString()
        );
        assertEquals(
            "Success{value=1.5151515151515151}",
            function.apply( "Ada" ).apply( 10.0 ).toString()
        );
        assertEquals(
            "Failure{exception=java.lang.IllegalStateException: divisor is 0}",
            function.apply( "Pascal" ).apply( 10.0 ).toString()
        );
        assertEquals(
            "Empty{}",
            function.apply( "Eric" ).apply( 10.0 ).toString()
        );
    }




}
