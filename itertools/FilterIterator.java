package itertools;

import java.util.Iterator;
import java.util.function.Predicate;
import java.util.NoSuchElementException;

public class FilterIterator<T> implements Iterator<T> {

    private Iterator<T> it;
    private Predicate<T> pred;
    private T nextElement;
    private boolean needNext = true;

    public FilterIterator(Iterator<T> it, Predicate<T> pred) {
        this.it = it;
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(!needNext) return true;
        do {
            if(!it.hasNext()) return false;
            nextElement = it.next();
        } while(!pred.test(nextElement));
        needNext = false;
        return true;
    }

    @Override
    public T next() {
        needNext = true;
        return nextElement;
    }
}
