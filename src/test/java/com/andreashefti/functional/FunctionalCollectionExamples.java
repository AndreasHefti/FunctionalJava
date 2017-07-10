package com.andreashefti.functional;

import org.junit.Test;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntConsumer;

import static com.andreashefti.functional.FunctionUtils.Collection.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Created by andreashefti on 04.07.17.
 */
public class FunctionalCollectionExamples {

    @Test
    public void creatingImmutableLists() {
        List<Integer> emptyList = list();
        List<Integer> list0 = list( 1 );
        List<Integer> list1 = list( 1, 2, 3 );

        assertEquals( "[]", emptyList.toString() );
        assertEquals( "[1]", list0.toString() );
        assertEquals( "[1, 2, 3]", list1.toString() );

        try {
            list1.add( 4 );
            fail( "Exception expected here" );
        } catch ( UnsupportedOperationException e ) {
            assertEquals( "UnsupportedOperationException", e.getClass().getSimpleName() );
        }
    }

    @Test
    public void listHeadTailAndCopy() {
        List<Integer> list = list( 1, 2, 3 );

        List<Integer> list1 = copy( list );
        List<Integer> list2 = tail( list );
        List<Integer> list3 = tail( list2 );
        Integer firstValue = head( list );

        assertEquals( "[1, 2, 3]", list.toString() );
        assertEquals( "[1, 2, 3]", list1.toString() );
        assertEquals( "[2, 3]", list2.toString() );
        assertEquals( "[3]", list3.toString() );
        assertEquals( "1", firstValue.toString() );

        List<Integer> list4 = tail( list3 );
        assertEquals( "[]", list4.toString() );

        try {
            tail( list4 );
            fail( "Exception expected here" );
        } catch ( IllegalStateException e ) {}
    }

    @Test
    public void testApplyEffect() {
        List<Integer> list = list( 1, 2, 3, 4, 5 );

        final StringBuilder out = new StringBuilder();
        Effect<Integer> effect = x -> out.append( x );

        applyEffect( list, effect );

        assertEquals( "12345", out.toString() );
    }

    @Test
    public void testMapImperative() {
        List<Integer> list = list( 1, 2, 3, 4, 5 );

        List<String> result = mapImperative( list, String::valueOf );
        assertEquals( "[1, 2, 3, 4, 5]",  result.toString() );


    }

    @Test
    public void testPrepend() {
        List<Integer> list = list( 1, 2, 3, 4, 5 );

        assertEquals( "[0, 1, 2, 3, 4, 5]", prepend( list, 0 ).toString() );
        assertEquals( "[0, 1, 2, 3, 4, 5]", prependFoldLeft( list, 0 ).toString() );
    }

    @Test
    public void testReverse() {
        List<Integer> list = list( 1, 2, 3, 4, 5 );
        assertEquals( "[5, 4, 3, 2, 1]", reverseImperative( list ).toString() );
        assertEquals( "[5, 4, 3, 2, 1]", reversePrepend( list ).toString() );
        assertEquals( "[5, 4, 3, 2, 1]", reverseFoldLeft( list ).toString() );
    }

    @Test
    /** This demonstrates the order of folding for left- and right-fold operations */
    public void foldOrder() {
        // The test List
        List<Integer> list = list( 1, 2, 3, 4, 5 );

        // Left Fold
        Function<String, Function<Integer, String>> fLeft = s -> i -> "(" + s + " + " + i + ")";
        assertEquals( "(((((identity + 1) + 2) + 3) + 4) + 5)", foldLeftImperative( list, "identity", fLeft ) );
        // Right Fold
        Function<Integer, Function<String, String>> fRight = s -> i -> "(" + s + " + " + i + ")";
        assertEquals( "(1 + (2 + (3 + (4 + (5 + identity)))))", foldRightImperative( list, "identity", fRight ) );

        // also for recursive implementations:
        assertEquals( "(((((identity + 1) + 2) + 3) + 4) + 5)", foldLeftRecursive( list, "identity", fLeft ) );
        assertEquals( "(1 + (2 + (3 + (4 + (5 + identity)))))", foldRightRecursive( list, "identity", fRight ) );
    }

    @Test
    /** This demonstrates the disadvantages of stack based recursive implementation of left- and right-Fold
     *  operation. Since recursive calls (even tail recursive calls) in Java are Stack based they have the
     *  limit of the memory for the Stack within the running JVM
     */
    public void foldRecursive() {

    }


}
