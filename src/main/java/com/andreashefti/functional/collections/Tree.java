package com.andreashefti.functional.collections;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by andreashefti on 24.07.17.
 */
public class Tree<T> {

    final Node<T> rootNode;

    public Tree( T rootValue ) {
        rootNode = new Node( rootValue );
    }

    public static class Node<T> {

        final T value;
        private List<Node<T>> children;

        public Node( T value ) {
            this.value = value;
            children = new ArrayList<>();
        }
    }
}
