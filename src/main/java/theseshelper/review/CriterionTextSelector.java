package theseshelper.review;

import java.util.*;
import java.util.function.*;

import org.apache.commons.math3.fraction.*;

public class CriterionTextSelector implements Function<BigFraction, String> {

    private final String defaultText;

    private final List<CriterionText> texts;

    public CriterionTextSelector(final List<CriterionText> texts, final String defaultText) {
        this.defaultText = defaultText;
        this.texts = new ArrayList<CriterionText>(texts);
        Collections.sort(this.texts);
    }

    @Override
    public String apply(final BigFraction evaluation) {
        for (final CriterionText text : this.texts) {
            if (evaluation.compareTo(text.achieved()) >= 0) {
                return text.text();
            }
        }
        return this.defaultText;
    }

}
