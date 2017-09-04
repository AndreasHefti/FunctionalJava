package com.andreashefti.functional;

/**
 * Created by andreashefti on 25.08.17.
 */
public final class Tuple<T, U> {

    public final T left;
    public final U right;

    private final int hash;

    public Tuple( T left, U right ) {
        this.left = left;
        this.right = right;

        int result = left != null ? left.hashCode() : 0;
        result = 31 * result + ( right != null ? right.hashCode() : 0 );
        hash = result;
    }

    @Override
    public final boolean equals( Object o ) {
        if( this == o ) return true;
        if( o == null || getClass() != o.getClass() ) return false;

        Tuple<?, ?> tuple = (Tuple<?, ?>) o;

        if( left != null ? !left.equals( tuple.left ) : tuple.left != null ) return false;
        return right != null ? right.equals( tuple.right ) : tuple.right == null;
    }

    @Override
    public final int hashCode() {
        return hash;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                   "left=" + left +
                   ", right=" + right +
                   '}';
    }
}
