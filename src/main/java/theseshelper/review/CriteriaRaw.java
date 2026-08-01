package theseshelper.review;

import java.util.*;

public class CriteriaRaw extends TreeMap<String, CriterionTextSelectorRaw> {

    private static final long serialVersionUID = 1L;

    public CriteriaRaw() {
        super();
    }

    public CriteriaRaw(final Map<String, CriterionTextSelectorRaw> map) {
        super(map);
    }

    public Criteria toCriteria() {
        final Criteria result = new Criteria();
        for (final Map.Entry<String, CriterionTextSelectorRaw> entry : this.entrySet()) {
            result.put(
                entry.getKey(),
                new CriterionTextSelector(
                    entry.getValue().name(),
                    entry.getValue().prefix(),
                    entry.getValue().texts().stream().map(CriterionTextRaw::toCriterionText).toList(),
                    entry.getValue().defaulttext(),
                    entry.getValue().suffix()
                )
            );
        }
        return result;
    }

}
