package theseshelper;

import java.util.*;
import java.util.function.*;

public class LexicographicComparator<T> implements Comparator<T> {

    public static <A, B extends Comparable<B>> Comparator<A> toComparator(final Function<A, B> selector) {
        return new Comparator<A>() {

            @Override
            public int compare(final A o1, final A o2) {
                final B b1 = selector.apply(o1);
                final B b2 = selector.apply(o2);
                if (b1 == null) {
                    if (b2 == null) {
                        return 0;
                    }
                    return -1;
                }
                if (b2 == null) {
                    return 1;
                }
                return b1.compareTo(b2);
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
