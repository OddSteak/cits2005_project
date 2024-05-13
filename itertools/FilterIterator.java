package itertools;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.NoSuchElementException;

public class FilterIterator<T> implements Iterator<T> {

    private Iterator<T> it;
    private Predicate<T> pred;
    private T nextElement;
    private boolean needNew = true;

    public FilterIterator(Iterator<T> it, Predicate<T> pred) {
        this.it = it;
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(!needNew ) return true;
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
        needNew = true;
        return nextElement;
    }
}
