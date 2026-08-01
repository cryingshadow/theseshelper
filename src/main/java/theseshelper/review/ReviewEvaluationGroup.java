package theseshelper.review;

import java.util.*;

import org.apache.commons.math3.fraction.*;

public record ReviewEvaluationGroup(
    String title,
    BigFraction weight,
    List<ReviewEvaluation> evaluations,
    String starttext,
    Integer adjust,
    String space
) {

    public BigFraction evaluate(final BigFraction totalExpected, final BigFraction weightSum) {
        final BigFraction innerTotal = this.total(totalExpected, weightSum);
        final BigFraction innerWeightSum =
            this.evaluations().stream().map(ReviewEvaluation::weight).reduce(BigFraction.ZERO, (x, y) -> x.add(y));
        return
            this.evaluations()
            .stream()
            .map(e -> e.evaluate(innerTotal, innerWeightSum))
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y));
    }

    public boolean hasUnusedCriterion() {
        return this.evaluations().stream().anyMatch(ReviewEvaluation::unused);
    }

    public ReviewEvaluationGroupRaw toRaw() {
        return new ReviewEvaluationGroupRaw(
            this.title(),
            this.weight() == null ? "" : this.weight().toString(),
            this.evaluations() == null ? List.of() : this.evaluations().stream().map(ReviewEvaluation::toRaw).toList(),
            this.starttext(),
            this.adjust(),
            this.space()
        );
    }

    public BigFraction total(final BigFraction totalExpected, final BigFraction weightSum) {
        return this.weight().divide(weightSum).multiply(totalExpected);
    }

}
