package thesesstats;

import java.util.*;
import java.util.function.*;

public class LexicographicComparator<T> implements Comparator<T> {

    public static <A, B extends Comparable<B>> Comparator<A> toComparator(final Function<A, B> selector) {
        return new Comparator<A>() {

            @Override
            public int compare(final A o1, final A o2) {
                return selector.apply(o1).compareTo(selector.apply(o2));
            }

        };
    }

    private final List<Comparator<T>> comparators;

    @SafeVarargs
    public LexicographicComparator(final Comparator<T>... comparators) {
        this(List.of(comparators));
    }

    public LexicographicComparator(final List<Comparator<T>> comparators) {
        this.comparators = comparators;
    }

    @Override
    public int compare(final T o1, final T o2) {
        int compare = 0;
        for (final Comparator<T> comparator : this.comparators) {
            if (compare != 0) {
                return compare;
            }
            compare = comparator.compare(o1, o2);
        }
        return compare;
    }

}
