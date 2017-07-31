# functionalJava

Learning functional programming in java based on the book "Functional programming in Java" from PIERRE-YVES SAUMONT


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


_There is also another way to create more then single arity functions but within only the Function functional interface
by currying the functions. How to do that is described later in this exercise._


