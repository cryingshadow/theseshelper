package theseshelper.review;

import org.apache.commons.math3.fraction.*;

public record ReviewEvaluation(
    String criterion,
    BigFraction evaluation,
    BigFraction weight,
    String alternative,
    String additional,
    EvaluationMode mode
) {

    public BigFraction evaluate(final BigFraction total, final BigFraction weightSum) {
        switch (this.evaluationMode()) {
        case EXTRA:
            return this.unused() ? BigFraction.ZERO : this.evaluation();
        default:
            return
                this.unused() ?
                    BigFraction.ZERO :
                        this.weight().divide(weightSum).multiply(this.evaluation()).multiply(total);
        }
    }

    public EvaluationMode evaluationMode() {
        return this.mode() == null ? EvaluationMode.NORMAL : this.mode();
    }

    public ReviewEvaluationRaw toRaw() {
        return new ReviewEvaluationRaw(
            this.criterion(),
            this.evaluation() == null ? "" : this.evaluation().toString(),
            this.weight() == null ? "" : this.weight().toString(),
            this.alternative(),
            this.additional(),
            this.mode()
        );
    }

    public boolean unused() {
        return this.evaluation() == null;
    }

}
