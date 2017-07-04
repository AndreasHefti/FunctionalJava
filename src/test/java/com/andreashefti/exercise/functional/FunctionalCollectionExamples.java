package com.andreashefti.exercise.functional;

import org.junit.Test;

import java.util.List;
import java.util.function.Function;

import static com.andreashefti.exercise.functional.FunctionUtils.Collection.*;
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
    public void leftFold() {
        List<Integer> list = list( 1, 2, 3, 4, 5 );
        Function<String, Function<Integer, String>> f = s -> i -> "(" + s + " + " + i + ")";

        assertEquals( "((((( + 1) + 2) + 3) + 4) + 5)", foldLeft( list, "", f ) );
    }


}
