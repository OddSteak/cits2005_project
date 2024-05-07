package itertools;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Function;

public class MapIterator<T, R> implements Iterator<R> {

    private Iterator<T> it;
    private Function<T, R> f;

    public MapIterator(Iterator<T> it, Function<T, R> f) {
        this.it = it;
        this.f = f;
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public R next() {
        if(!hasNext()) throw new NoSuchElementException();
        return f.apply(it.next());
    }
}
