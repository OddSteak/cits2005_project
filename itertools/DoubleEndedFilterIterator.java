package itertools;

import java.util.function.Predicate;
import java.util.NoSuchElementException;

public class DoubleEndedFilterIterator<T> implements DoubleEndedIterator<T> {

    private DoubleEndedIterator<T> it;
    private Predicate<T> pred;
    private T frontElement;
    private boolean needNew = true;

    public DoubleEndedFilterIterator(DoubleEndedIterator<T> it, Predicate<T> pred) {
        this.it = it;
        this.pred = pred;
    }

    @Override
    public boolean hasNext() {
        if(!needNew ) return true;
        do {
            if(!it.hasNext()) return false;
            frontElement = it.next();
        } while(!pred.test(frontElement));
        needNew = false;
        return true;
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        needNew = true;
        return frontElement;
    }

    @Override
    public T reverseNext() {
        if(!it.hasNext()) throw new NoSuchElementException();
        return it.reverseNext();
    }
}
