package theseshelper.review;

import org.apache.commons.math3.fraction.*;

public record CriterionText(BigFraction achieved, String text) implements Comparable<CriterionText> {

    @Override
    public int compareTo(final CriterionText o) {
        if (o.achieved() == null) {
            if (this.achieved() == null) {
                return 0;
            }
            return 1;
        }
        if (this.achieved() == null) {
            return -1;
        }
        return o.achieved().compareTo(this.achieved());
    }

    public CriterionTextRaw toRaw() {
        return new CriterionTextRaw(this.achieved() == null ? "" : this.achieved().toString(), this.text());
    }

}
