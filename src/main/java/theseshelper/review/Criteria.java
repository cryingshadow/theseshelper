package theseshelper.review;

import java.io.*;
import java.util.*;

import theseshelper.*;

public class Criteria extends LinkedHashMap<String, CriterionTextSelector> {

    private static final long serialVersionUID = 1L;

    public static Criteria parseCriteria(final File criteriaFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(criteriaFile))) {
            return Main.GSON.fromJson(reader, CriteriaRaw.class).toCriteria();
        }
    }

    public Criteria() {
        super();
    }

    public Criteria(final Map<String, CriterionTextSelector> map) {
        super(map);
    }

    public CriteriaRaw toRaw() {
        final CriteriaRaw result = new CriteriaRaw();
        for (final Map.Entry<String, CriterionTextSelector> entry : this.entrySet()) {
            result.put(
                entry.getKey(),
                new CriterionTextSelectorRaw(
                    entry.getValue().prefix,
                    entry.getValue().texts.stream().map(CriterionText::toRaw).toList(),
                    entry.getValue().defaultText,
                    entry.getValue().suffix
                )
            );
        }
        return result;
    }

}
