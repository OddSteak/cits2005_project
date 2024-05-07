package itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.BiFunction;

public class ReduceIterator<T, R> implements Iterator<R> {

    private Iterator<T> it;
    private BiFunction<R, T, R> f;
    private R value;

    public ReduceIterator(Iterator<T> it, R init, BiFunction<R, T, R> f) {
        this.it = it;
        this.f = f;
        value = init;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public R next() {
        if(!hasNext()) throw new NoSuchElementException();
        value = f.apply(value, it.next());
        return value;
    }
}
