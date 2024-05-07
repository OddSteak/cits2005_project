package itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TakeIterator<T> implements Iterator<T> {

    private Iterator<T> it;
    private int count, nextCount;

    public TakeIterator(Iterator<T> it, int count) {
        this.it = it;
        this.count = count;
        this.nextCount = 0;
    }

    @Override
    public boolean hasNext() {
        return nextCount < count;
    }

    @Override
    public T next() {
        if(!hasNext()) throw new NoSuchElementException();
        nextCount++;
        return it.next();
    }
}
