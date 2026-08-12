package theseshelper.review;

import java.util.*;

import org.apache.commons.math3.fraction.*;

public record ReviewEvaluationGroup(
    String title,
    BigFraction weight,
    List<ReviewEvaluation> evaluations,
    String starttext,
    Integer adjust,
    String space,
    Boolean diagram
) {

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
            this.space(),
            this.diagram()
        );
    }

}
