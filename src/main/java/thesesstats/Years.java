package thesesstats;

import java.time.*;
import java.util.*;

public class Years extends LinkedList<Integer> {

    private static final long serialVersionUID = 1L;

    public Years(final int year) {
        if (year < 1) {
            final int yearToday = Year.now().getValue();
            for (int currentYear = 2022; currentYear <= yearToday; currentYear++) {
                this.add(currentYear);
            }
        } else {
            this.add(year);
        }
    }

}
