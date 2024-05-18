package itertools;

import java.util.NoSuchElementException;

/** An iterator in the reverse order of the one given */
public class ReversedIterator<T> implements DoubleEndedIterator<T> {

    private DoubleEndedIterator<T> it;

    public ReversedIterator(DoubleEndedIterator<T> it) {
        this.it = it;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        return it.reverseNext();
    }

    @Override
    public T reverseNext() {
        if(!hasNext()) throw new NoSuchElementException();
        return it.next();
    }
}
