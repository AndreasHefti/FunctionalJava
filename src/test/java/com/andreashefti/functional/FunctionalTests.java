package com.andreashefti.functional;

import org.junit.Test;

import java.util.function.Consumer;

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
}
