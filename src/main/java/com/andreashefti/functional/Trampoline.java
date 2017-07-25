package com.andreashefti.functional;

import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * Created by andreashefti on 24.07.17.
 */
@FunctionalInterface
public interface Trampoline<T> extends Supplier<Trampoline<T>> {

    default boolean isComplete() {
        return false;
    }

    default T result() {
        throw new Error( "not implemented" );
    }

    default T invoke() {
        return Stream.iterate( this, Trampoline::get )
                   .filter( Trampoline::isComplete )
                   .findFirst()
                   .get()
                   .result();
    }


    static <T> Trampoline<T> call( final Trampoline<T> nextCall ) {
        return nextCall;
    }

    static <T> Trampoline<T> done( final T value ) {
        return new Trampoline<T>() {
            @Override
            public boolean isComplete() {
                return true;
            }
            @Override public T result() {
                return value;
            }
            @Override
            public Trampoline<T> get() {
                throw new Error( "not implemented" );
            }
        };
    }
}
