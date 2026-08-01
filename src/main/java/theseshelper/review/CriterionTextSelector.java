package theseshelper.review;

import java.util.*;
import java.util.function.*;

import org.apache.commons.math3.fraction.*;

public class CriterionTextSelector implements Function<BigFraction, String> {

    final String defaultText;

    final String prefix;

    final String suffix;

    final List<CriterionText> texts;

    public CriterionTextSelector(
        final String prefix,
        final List<CriterionText> texts,
        final String defaultText,
        final String suffix
    ) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.defaultText = defaultText;
        this.texts = new ArrayList<CriterionText>(texts);
        Collections.sort(this.texts);
    }

    @Override
    public String apply(final BigFraction evaluation) {
        final StringBuilder result = new StringBuilder();
        result.append(this.prefix);
        boolean found = false;
        for (final CriterionText text : this.texts) {
            if (evaluation.compareTo(text.achieved()) >= 0) {
                result.append(text.text());
                found = true;
                break;
            }
        }
        if (!found) {
            result.append(this.defaultText);
        }
        result.append(this.suffix);
        return result.toString();
    }

}
