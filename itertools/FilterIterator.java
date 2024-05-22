package itertools;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.function.Predicate;

/**
* iterator over the elements of a given iterator with those elements that do not satisfy a given {@link Predicate} dropped.
*/
public class FilterIterator<T> implements Iterator<T> {

    private Iterator<T> it;
    private Predicate<T> pred;

    // tracks if hasNext() needs to find a new element
    private boolean needNew = true;
    // store the next element to return
    private T nextElement;

    public FilterIterator(Iterator<T> it, Predicate<T> pred) {
        this.it = it;
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(!needNew ) return true;

        // loop to find a new element that satisfies the predicate
        do {
            if(!it.hasNext()) return false;
            nextElement = it.next();
        } while(!pred.test(nextElement));

        needNew = false;
        return true;
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        // need a new element since we are consuming nextElement
        needNew = true;
        return nextElement;
    }
}
