package theseshelper.review;

import java.util.*;

public class Criteria extends LinkedHashMap<String, CriterionTextSelector> {

    private static final long serialVersionUID = 1L;

    public Criteria() {
        super();
    }

    public Criteria(final Map<String, CriterionTextSelector> map) {
        super(map);
    }

}
