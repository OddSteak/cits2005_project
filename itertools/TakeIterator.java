package itertools;

import java.util.NoSuchElementException;
import java.util.Iterator;

/** An iterator over the given number of elements taken from a given iterator or as many as it contains, if less than that number*/
public class TakeIterator<T> implements Iterator<T> {

    private Iterator<T> it;
    private int count;
    private int numTaken = 0; // tracks the number of elements taken

    public TakeIterator(Iterator<T> it, int count) {
        this.it = it;
        this.count = count;
    }

    @Override
    public boolean hasNext() {
        if(!it.hasNext()) return false;

        // unless the iterator has ended,
        // there remains elements to return as long as we have taken less than count.
        return numTaken < count;
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        numTaken++;
        return it.next();
    }
}
