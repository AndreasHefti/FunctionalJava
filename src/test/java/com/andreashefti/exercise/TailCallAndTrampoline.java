package com.andreashefti.exercise;

import com.andreashefti.functional.TailCall;
import com.andreashefti.functional.Trampoline;
import org.junit.Test;

import java.math.BigInteger;
import java.util.function.Function;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.fail;

public class TailCallAndTrampoline {

    // **** recursive factorial implementations with TailCall ****

    @Test
    public void factorialWithTailCall() {
        assertEquals( "120", String.valueOf( factorialTailCall( 5 ) ) );
        assertEquals( "1819206320", String.valueOf( factorialTailCall( 20000 ) ).substring( 0, 10 ) );
    }

    public static BigInteger factorialTailCall( final int number ) {
        System.out.println( "factorial called" );
        TailCall<BigInteger> f = factorialTailRec( BigInteger.ONE, BigInteger.valueOf( number ) );
        System.out.println( "tail-call created --> eval" );
        return f.eval();
    }

    private static TailCall<BigInteger> factorialTailRec( final BigInteger factorial, final BigInteger number ) {
        if ( number.equals( BigInteger.ONE ) ) {
            System.out.println( "return" );
            return TailCall.ret( factorial );
        } else {
            System.out.println( "suspend:" + number  );
            return TailCall.sus( () -> factorialTailRec( factorial.multiply( number ), number.subtract( BigInteger.ONE ) ) );
        }
    }

    // **** recursive factorial implementation with Trampoline ****

    @Test
    public void factorialWithTrampoline() {
        assertEquals( "120", String.valueOf( factorialTrampoline( 5 ) ) );
        //assertEquals( "1819206320", String.valueOf( factorialTrampoline( 20000 ) ).substring( 0, 10 ) );
    }

    public static BigInteger factorialTrampoline( final int number ) {
        System.out.println( "factorial called" );
        Trampoline<BigInteger> f = factorialTrampoline( BigInteger.ONE, BigInteger.valueOf( number ) );
        System.out.println( "trampoline created --> invoke" );
        return f.invoke();
    }

    private static Trampoline<BigInteger> factorialTrampoline( final BigInteger factorial, final BigInteger number ) {
        if ( number.equals( BigInteger.ONE ) ) {
            System.out.println( "return" );
            return Trampoline.done( factorial );
        } else {
            System.out.println( "suspend:" + number  );
            return Trampoline.call( () -> factorialTrampoline( factorial.multiply( number ), number.subtract( BigInteger.ONE ) ) );
        }
    }

    // Conclusion: From the implementation point of view and also from the processing point of viw they are the quite the same.
    //             The implementations of Trampoline seems better because it defines a functional interface that is extended from the
    //             Supplier interface with default implementations for the none functional methods and it uses Java 8 Streams on
    //             invoke witch makes it more functional and also more readable.



    // **** Naive fibonacci implementation ****

    @Test
    public void naiveFibonacci() {
        assertEquals( "0", String.valueOf( naiveFibonacci( 0 ) ) );
        assertEquals( "1", String.valueOf( naiveFibonacci( 1 ) ) );
        assertEquals( "1", String.valueOf( naiveFibonacci( 2 ) ) );
        assertEquals( "2", String.valueOf( naiveFibonacci( 3 ) ) );
        assertEquals( "3", String.valueOf( naiveFibonacci( 4 ) ) );
        assertEquals( "5", String.valueOf( naiveFibonacci( 5 ) ) );
        assertEquals( "8", String.valueOf( naiveFibonacci( 6 ) ) );
        assertEquals( "13", String.valueOf( naiveFibonacci( 7 ) ) );
        assertEquals( "21", String.valueOf( naiveFibonacci( 8 ) ) );
        assertEquals( "34", String.valueOf( naiveFibonacci( 9 ) ) );
        assertEquals( "55", String.valueOf( naiveFibonacci( 10 ) ) );

        // so far so good but going further...
        // the naiveFibonacci( 100 ) will already take a long time and
        // the naiveFibonacci( 5000 ) will take the rest of the time of our solar system... maybe longer
        // and there will be an arithmetic overflow resulting in negative or wrong numbers

        // So how to do it better...
        // fist try to make the function tail call recursive
    }

    public static int naiveFibonacci( int number ) {
        if ( number == 0 || number == 1 ) {
            return number;
        }
        return naiveFibonacci( number - 1 ) + naiveFibonacci( number - 2 );
    }

    // **** Tail call recursive fibonacci implementation ****

    @Test
    public void tailCallRecursiveFibonacci() {
        assertEquals( "55", String.valueOf( tailCallFibonacci( 10 ) ) );

        // 100 no problem now:
        assertEquals( "354224848179261915075", String.valueOf( tailCallFibonacci( 100 ) ) );
        // 5000 also no problem now but it is a huge number:
        assertEquals( "3878968454388325633701916308325905312082127714646245106160597214895550139044037097010822916462210669479293452858882973813483102008954982940361430156911478938364216563944106910214505634133706558656238254656700712525929903854933813928836378347518908762970712033337052923107693008518093849801803847813996748881765554653788291644268912980384613778969021502293082475666346224923071883324803280375039130352903304505842701147635242270210934637699104006714174883298422891491273104054328753298044273676822977244987749874555691907703880637046832794811358973739993110106219308149018570815397854379195305617510761053075688783766033667355445258844886241619210553457493675897849027988234351023599844663934853256411952221859563060475364645470760330902420806382584929156452876291575759142343809142302917491088984155209854432486594079793571316841692868039545309545388698114665082066862897420639323438488465240988742395873801976993820317174208932265468879364002630797780058759129671389634214252579116872755600360311370547754724604639987588046985178408674382863125", String.valueOf( tailCallFibonacci( 5000 ) ) );

        // but the problem with the stack still exists
        try {
            tailCallFibonacci( 100000 );
            fail( "StackOverflowError expected" );
        } catch ( StackOverflowError soe ) {

        }
    }

    public static BigInteger tailCallFibonacci( int number ) {
        return tailCallFibonacci_( BigInteger.valueOf( number ), BigInteger.ONE, BigInteger.ZERO );
    }

    private static BigInteger tailCallFibonacci_( BigInteger number, BigInteger acc1, BigInteger acc2 ) {
        if ( number.equals( BigInteger.ZERO ) ) {
            return BigInteger.ZERO;
        } else if ( number.equals( BigInteger.ONE ) ) {
            return acc1.add( acc2 );
        } else {
            return tailCallFibonacci_( number.subtract( BigInteger.ONE ), acc2, acc1.add( acc2 ) );
        }
    }

    // **** Tail call recursive fibonacci implementation using the Tampoline to eliminate the stack problem ****

    @Test
    public void trampolineFibonacci() {
        assertEquals( "55", String.valueOf( trampolineFibonacci( 10 ) ) );

        // 100 no problem now:
        assertEquals( "354224848179261915075", String.valueOf( trampolineFibonacci( 100 ) ) );
        // 5000 also no problem now but it is a huge number:
        assertEquals(
            "3878968454388325633701916308325905312082127714646245106160597214895550139044037097010822916462210669479293452858882973813483102008954982940361430156911478938364216563944106910214505634133706558656238254656700712525929903854933813928836378347518908762970712033337052923107693008518093849801803847813996748881765554653788291644268912980384613778969021502293082475666346224923071883324803280375039130352903304505842701147635242270210934637699104006714174883298422891491273104054328753298044273676822977244987749874555691907703880637046832794811358973739993110106219308149018570815397854379195305617510761053075688783766033667355445258844886241619210553457493675897849027988234351023599844663934853256411952221859563060475364645470760330902420806382584929156452876291575759142343809142302917491088984155209854432486594079793571316841692868039545309545388698114665082066862897420639323438488465240988742395873801976993820317174208932265468879364002630797780058759129671389634214252579116872755600360311370547754724604639987588046985178408674382863125",
            String.valueOf( trampolineFibonacci( 5000 ) )
        );

        // also no problem
        try {
            trampolineFibonacci( 100000 );
        } catch ( StackOverflowError soe ) {
            fail( "no StackOverflowError expected" );
        }
    }

    public static BigInteger trampolineFibonacci( int number ) {
        return trampolineFibonacci_(
            BigInteger.valueOf( number ),
            BigInteger.ONE, BigInteger.ZERO
        ).invoke();
    }

    private static Trampoline<BigInteger> trampolineFibonacci_( BigInteger number, BigInteger acc1, BigInteger acc2 ) {
        if ( number.equals( BigInteger.ZERO ) ) {
            return Trampoline.done( BigInteger.ZERO );
        } else if ( number.equals( BigInteger.ONE ) ) {
            return Trampoline.done( acc1.add( acc2 ) );
        } else {
            return Trampoline.call( () -> trampolineFibonacci_( number.subtract( BigInteger.ONE ), acc2, acc1.add( acc2 ) ) );
        }
    }

    // Next Problem: There are often recursive methods used within collections structures for different reasons like search,
    //               map, reduce, filter and so on.
    //               Normally the recursive methods for Tree structures uses a recursive call for every child of a node
    //               and this is not what we can resolve with a recursive tail call or a Trampoline. But there must be
    //               other ways to gain performance and solving the stack problem for recursive methods within a Tree structure.
    //               Maybe by flattening the collections structure using Java Streams...


}
