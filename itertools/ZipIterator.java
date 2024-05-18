package itertools;

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.util.function.BiFunction;

/** returns an iterator over the results of combining each pair of
 * elements from a pair of given iterators using a given function. */
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
        // end the iterator when either of the given iterators end
        return lit.hasNext() && rit.hasNext();
    }

    @Override
    public R next() {
        if(!hasNext()) throw new NoSuchElementException();
        return f.apply(lit.next(), rit.next());
    }
}
