package theseshelper.review;

import java.util.*;

public record ReviewEvaluationGroupRaw(
    String title,
    String weight,
    List<ReviewEvaluationRaw> evaluations,
    String starttext,
    Integer adjust,
    String space,
    Boolean diagram
) {

    public ReviewEvaluationGroup toEvaluationGroup() {
        return new ReviewEvaluationGroup(
            this.title(),
            Review.parseRationalNumber(this.weight()),
            this.evaluations() == null ?
                null :
                    this.evaluations().stream().map(ReviewEvaluationRaw::toEvaluation).toList(),
            this.starttext(),
            this.adjust(),
            this.space(),
            this.diagram()
        );
    }

}
