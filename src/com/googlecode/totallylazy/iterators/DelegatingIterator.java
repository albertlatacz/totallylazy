package com.googlecode.totallylazy.iterators;

import java.util.Iterator;

public class DelegatingIterator<T> extends ReadOnlyIterator<T> {
    private final Iterator<T> iterator;

    public DelegatingIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public boolean hasNext() {
        return iterator.hasNext();
    }

    public T next() {
        return iterator.next();
    }
}
