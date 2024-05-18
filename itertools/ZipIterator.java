package itertools;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.function.BiFunction;

public class ZipIterator<T, U, R> implements Iterator<R> {

    private Iterator<T> lit;
    private Iterator<U> rit;
    private BiFunction<T, U, R> f;

    public ZipIterator(Iterator<T> lit, Iterator<U> rit, BiFunction<T, U, R> f) {
        this.lit = lit;
        this.rit = rit;
        this.f = f;
    }

    @Override
    public boolean hasNext() {
        return lit.hasNext() && rit.hasNext();
    }

    @Override
    public R next() {
        if(!hasNext()) throw new NoSuchElementException();
        return f.apply(lit.next(), rit.next());
    }
}
