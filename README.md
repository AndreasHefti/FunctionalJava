# Functional Java

Learning functional programming in java based on the books:
* Functional programming in Java from PIERRE-YVES SAUMONT
* Functional programming in Java from Venkat Subramaniam


## [1 Functional Basics](https://github.com/AndreasHefti/functionalJava/blob/master/src/test/java/com/andreashefti/exercise/_1_BasicFunctional.java)
### 1.0 Functional Interfaces

* A functional interface is a interface with only one abstract method.
* A functional interface my have other methods with default implementations.
* A functional interface should be marked with the @FunctionalInterface Annotation.
* A functional interface can be used within a lambda expression.
 
One widely used example of an functional interface is java.util.function.Function that has an abstract method
R apply ( T t ) witch defines a function that receives a value of type T and gives back a value of type R.

There is also a java.util.function.BiFunction that defines a two-arity function that receives two values as parameter.
Java goes no further with the arity of functions but we can defines three-arity function within a functional interface
ourselves as an example, the TriFunction.


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


_There is also another way to create more then single arity functions but within only the java.util.function.Function functional interface
by currying the functions. How to do that is described later in this exercise._

### 1.1 Most used functional interfaces of Java 8

- **java.util.function.Function:**

Defines a polymorphic one-arity function with one function-parameter of type T
and a return value of type R.

Use the higher order function "compose" that is implemented as a default method to compose another
Function before a given Function. The composed Function must have the same return type (or substitute of)
as the given Function instances parameter type. This results in a new Function with parameter type of
the composed Function and the the original return type

Function<V,R> = Function<T,R>.compose( Function<V,T> )
Function<V,R> = Function<T,R>.compose( Function<? super V,? extends T> )
 
Use the higher order function "andThen" that is also implemented as default method to compose another
Function after a given Function.

Function<T,V> = Function<T,R>.compose( Function<R,V> )
Function<T,V> = Function<T,R>.compose( Function<? super R,? extends V> )

Use the static "identity" method to get an identity function that gives back what it receives.


- **java.util.function.BiFunction:**

Defines a polymorphic two-arity function with function-parameter of type T and U
and a return value of type R.

There are no functional interfaces with more arity defined in the standard JDK but
one can implement it like the TriFunction example above or within function currying
described later in this examples.


- **java.util.function.Predicate:**

A specialisation of Function with a boolean as return type.
This is mostly used to replace or abstract a imperative if statement.

Use the higher order function "and" to compose another Predicate with a given one by a logical and.
Use the higher order function "or" to to compose another Predicate with a given one by a logical or.
Use the function "negate" to get a new Predicate that represents a logical not of the original one.


- **java.util.function.Supplier:**

A Supplier is a specialised Function that has no parameter and just a return value. It supplies something.


- **[java.util.function.Consumer:](https://github.com/AndreasHefti/functionalJava/blob/master/src/main/java/com/andreashefti/functional/Effect.java)**

A Consumer is a specialised Function that has no return value and only one parameter. It consumes a value and
dies something with it. This something mostly is an effect like logging, printing out, write to...

So if there is an effect that needs to be applied, it make sense to call it like this:
  - com.andreashefti.functional.Effect

_NOTE: There a lot other functional interface definitions within the JDK's java.util.function package
but most of them are specialisations of the above just dealing with primitive types or higher arity._

    @Test
    public void mostUsedFunctionalInterfacesOfJava8() {

        // NOTE: this are still implementations with anonymous inner classes. Later we will use Lambda Expressions for that
        
        // *** java.util.function.Function:
        
        Function<String, Integer> length = new Function<String, Integer>() {
            @Override
            public Integer apply( String s ) {
                return ( s == null )? 0 : s.length();
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
        
        
        
        // *** java.util.function.BiFunction
        
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
        
        // *** java.util.function.Predicate
        
        Predicate<String> lengthOf5 = new Predicate<String>() {
            @Override
            public boolean test( String s ) {
                return ( s == null )? false : s.length() == 5;
            }
        };
        
        Predicate<String> startsWithA = new Predicate<String>() {
            @Override
            public boolean test( String s ) {
                return ( s == null )? false : s.startsWith( "A" );
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
        
        
        
        // *** java.util.function.Supplier
        
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
        
        
        // *** java.util.function.Consumer and com.andreashefti.functional.Effect
        
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