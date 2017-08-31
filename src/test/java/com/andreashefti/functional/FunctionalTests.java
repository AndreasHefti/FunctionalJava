package com.andreashefti.functional;

import org.junit.Test;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Created by andreashefti on 16.08.17.
 */
public class FunctionalTests {

    @Test
    public void test1() {
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

    @Test
    public void flatMap() {
        List<Character> list = Stream.of( "One", "Two", "Tree", "Four" )
           .flatMap( s -> s.chars().mapToObj( i -> (char) i ) )
           .collect( Collectors.toList() );

        System.out.println( list );

        List<Integer> years = Stream.of( 2017, 2018  ).collect( Collectors.toList() );
        List<Month> months = Stream.of( Month.AUGUST, Month.DECEMBER ).collect( Collectors.toList() );
        List<Integer> daysOfMonth = Stream.of( 1, 4, 10, 23 ).collect( Collectors.toList() );

        Collection<LocalDate> dates = months
          .stream()
          .flatMap( m -> daysOfMonth.stream().map( d -> LocalDate.of( 2017, m, d ) ) )
          .collect( Collectors.toList() );

        System.out.println( dates );

        Collection<LocalDate> dates2 = years
          .stream()
          .flatMap( y -> months.stream().map( m -> new Tuple<>( y, m ) ) )
          .flatMap( tuple -> daysOfMonth.stream().map( d -> LocalDate.of( tuple.left, tuple.right, d ) ) )
          .collect( Collectors.toList() );

        System.out.println( dates2 );

    }
}
