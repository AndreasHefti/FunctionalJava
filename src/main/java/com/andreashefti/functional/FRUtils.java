package com.andreashefti.functional;

import java.util.function.Predicate;


public abstract class FRUtils {

    public static final <T> Predicate<T> not( Predicate<T> predicate ) {
        return predicate.negate();
    }

    public static abstract class _String {

        public static final boolean isNotEmpty( String s ) {
            if ( s == null ) {
                return false;
            }
            return !s.isEmpty();
        }
    }

}
