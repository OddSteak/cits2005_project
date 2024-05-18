package itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/** A double ended iterator with elements of a given iterator that do not satisfy a given predicate dropped */
public class ReversedFilterIterator<T> implements Iterator<T> {

    private DoubleEndedIterator<T> it;
    private Predicate<T> pred;
    private T nextElement;
    private boolean needNew = true;

    public ReversedFilterIterator(DoubleEndedIterator<T> it, Predicate<T> pred) {
        this.it = it;
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(!needNew ) return true;

        // loop to find the next element that satisfies the predicate
        do {
            if(!it.hasNext()) return false;
            nextElement = it.reverseNext();
        } while(!pred.test(nextElement));

        needNew = false;
        return true;
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        needNew = true;
        return nextElement;
    }
}
