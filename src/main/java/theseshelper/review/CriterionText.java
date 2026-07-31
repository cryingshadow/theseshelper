package theseshelper.review;

import org.apache.commons.math3.fraction.*;

public record CriterionText(BigFraction achieved, String text) implements Comparable<CriterionText> {

    @Override
    public int compareTo(final CriterionText o) {
        return o.achieved().compareTo(this.achieved());
    }

}
