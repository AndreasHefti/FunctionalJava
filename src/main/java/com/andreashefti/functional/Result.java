package com.andreashefti.functional;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Created by andreashefti on 16.10.17.
 */
public abstract class Result<T> implements Serializable {

    private static final Result EMPTY = new Empty<>();

    private Result() {}

    public abstract boolean isPresent();
    public abstract T get();
    public abstract T getOrElse( final T defaultValue );
    public abstract T getOrElse( final Supplier<T> defaultValue );
    public abstract Result<T> orElse( final Supplier<Result<T>> defaultValue );
    public abstract Optional<T> toOptional();


    private static final class Success<T> extends Result<T> {

        private final T value;

        public Success( T value ) {
            super();
            this.value = value;
        }

        @Override public boolean isPresent() { return true; }
        @Override public T get() { return value; }
        @Override public T getOrElse( T defaultValue ) { return value; }
        @Override public T getOrElse( Supplier<T> defaultValue ) { return value; }
        @Override public Result<T> orElse( Supplier<Result<T>> defaultValue ) { return success( value ); }
        @Override public Optional<T> toOptional() { return Optional.of( value ); }

        @Override
        public String toString() {
            return "Success{" +
                       "value=" + value +
                       '}';
        }
    }

    private static final class Empty<T> extends Result<T> {

        private Empty() {}

        @Override public boolean isPresent() { return false; }
        @Override public T get() { throw new IllegalStateException( "get called on empty Result" ); }
        @Override public T getOrElse( T defaultValue ) { return defaultValue; }
        @Override public T getOrElse( Supplier<T> defaultValue ) { return defaultValue.get(); }
        @Override public Result<T> orElse( Supplier<Result<T>> defaultValue ) { return defaultValue.get(); }
        @Override public Optional<T> toOptional() { return Optional.empty(); }

        @Override
        public String toString() {
            return "Empty{}";
        }
    }

    private static final class Failure<T> extends Result<T> {

        private final RuntimeException exception;

        public Failure( RuntimeException e ) {
            super();
            exception = e;
        }

        public Failure( Exception e ) {
            super();
            exception = new IllegalStateException( e.getMessage(), e );
        }

        public Failure( String message ) {
            super();
            exception = new IllegalStateException( message) ;
        }

        @Override public boolean isPresent() { return false; }
        @Override public T get() { throw exception; }
        @Override public T getOrElse( final T defaultValue ) { return defaultValue; }
        @Override public T getOrElse( final Supplier<T> defaultValue ) { return defaultValue.get(); }
        @Override public Result<T> orElse( Supplier<Result<T>> defaultValue ) { return defaultValue.get(); }
        @Override public Optional<T> toOptional() { return Optional.empty(); }

        @Override
        public String toString() {
            return "Failure{" +
                       "exception=" + exception +
                       '}';
        }
    }


    public final static <T> Result<T> failure( String message ) {
        return new Failure<>( message );
    }

    public final static <T> Result<T> failure( Exception e ) {
        return new Failure<>( e );
    }

    public final static <T> Result<T> failure( RuntimeException e ) {
        return new Failure<>( e );
    }


    public final static <T> Result<T> success( T value ) {
        return new Success<>( value );
    }

    @SuppressWarnings("unchecked")
    public final static <V> Result<V> empty() {
        return EMPTY;
    }

}
