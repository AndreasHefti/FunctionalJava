package com.andreashefti.functional;

/**
 * Created by andreashefti on 25.08.17.
 */
public class Tuple<T, U> {

    public final T left;
    public final U right;

    public Tuple( T left, U right ) {
        this.left = left;
        this.right = right;
    }
}
