package com.andreashefti.exercise.functional;


import org.junit.Test;

import java.util.function.Function;

import static org.junit.Assert.assertEquals;


public class FunctionCompositionTests {

    public final static Function<String, Integer> STRING_TO_INT = s -> Integer.valueOf( s );
    public final static Function<Integer, String> INT_TO_STRING = i -> String.valueOf( i );
    public final static Function<Integer, Integer> INCREMENT = i -> i + 1;
    public final static Function<Integer, Integer> DECREMENT = i -> i - 1;


    @Test
    public void simpleCompose() {

        assertEquals(
                 "2",
                 INT_TO_STRING
                         .compose( INCREMENT )
                         .compose( STRING_TO_INT )
                         .apply( "1" )
         );

        assertEquals(
                "2",


                INT_TO_STRING
                        .compose( INCREMENT )
                        .compose( STRING_TO_INT )
                        .apply( "1" )
        );

        assertEquals(
                "2",
                STRING_TO_INT
                    .andThen( INCREMENT )
                    .andThen( INT_TO_STRING )
                    .apply( "1" )
        );


        assertEquals(
            "22",
            STRING_TO_INT
                .andThen( INCREMENT )
                .andThen( INT_TO_STRING )
                .andThen( s -> s + 1 )          // NOTE: this concat the given string with 1 --> "string" + 1
                .andThen( STRING_TO_INT )
                .andThen( INCREMENT )
                .andThen( INT_TO_STRING )
                .apply( "1" )
        );
    }

    @Test
    public void curryingWithComposeAndHigherComposeConstants() {

        Function<Long, Double> f1 = a ->  a + 2.0;
        Function<Long, Double> f2 = a ->  a + 3.0;
        Function<Double, Integer> g1 = a -> (int) ( a * 3 );
        Function<Double, Integer> g2 = a -> (int) ( a * 4 );

        // currying the first parameter/function --> f1/f2 with compose

        Function<Function<Double, Integer>, Function<Long, Integer>> f1Applied = FunctionUtils.<Long, Double, Integer>compose().apply( f1 );
        Function<Function<Double, Integer>, Function<Long, Integer>> f2Applied = FunctionUtils.<Long, Double, Integer>compose().apply( f2 );
        Function<Long, Integer> hA = f1Applied.apply( g1 );
        Function<Long, Integer> hB = f1Applied.apply( g2 );
        Function<Long, Integer> hC = f2Applied.apply( g1 );
        Function<Long, Integer> hD = f2Applied.apply( g2 );

        assertEquals( "9", String.valueOf( hA.apply( 1L ) ) );
        assertEquals( "12", String.valueOf( hB.apply( 1L ) ) );
        assertEquals( "12", String.valueOf( hC.apply( 1L ) ) );
        assertEquals( "16", String.valueOf( hD.apply( 1L ) ) );

        // currying the second parameter/function --> g1/g2 with higherCompose

        Function<Function<Long, Double>, Function<Long, Integer>> g1Applied = FunctionUtils.<Long, Double, Integer>higherCompose().apply( g1 );
        Function<Function<Long, Double>, Function<Long, Integer>> g2Applied = FunctionUtils.<Long, Double, Integer>higherCompose().apply( g2 );
        hA = g1Applied.apply( f1 );
        hB = g1Applied.apply( f2 );
        hC = g2Applied.apply( f1 );
        hD = g2Applied.apply( f2 );

        assertEquals( "9", String.valueOf( hA.apply( 1L ) ) );
        assertEquals( "12", String.valueOf( hB.apply( 1L ) ) );
        assertEquals( "12", String.valueOf( hC.apply( 1L ) ) );
        assertEquals( "16", String.valueOf( hD.apply( 1L ) ) );
    }

}
