package itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

/** An iterator with elements of a given iterator reversed and the ones that do not satisfy a given predicate dropped */
public class ReversedFilterIterator<T> implements Iterator<T> {

    private DoubleEndedIterator<T> it;
    private Predicate<T> pred;
    // track if hasNext() needs to find a new element
    private boolean needNew = true;
    // store the next element to return
    private T nextElement;

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
