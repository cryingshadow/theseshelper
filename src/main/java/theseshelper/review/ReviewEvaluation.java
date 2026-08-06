package theseshelper.review;

import java.util.*;

import org.apache.commons.math3.fraction.*;

public record ReviewEvaluation(
    String criterion,
    BigFraction evaluation,
    BigFraction weight,
    String alternative,
    String additional,
    EvaluationMode mode
) {

    public ReviewEvaluation(final String criterion, final BigFraction weight) {
        this(criterion, null, weight, null, null, null);
    }

    public ReviewEvaluation(final String criterion, final BigFraction weight, final EvaluationMode mode) {
        this(criterion, null, weight, null, null, mode);
    }

    public BigFraction evaluate(final BigFraction total, final BigFraction weightSum) {
        if (this.unused()) {
            return BigFraction.ZERO;
        }
        switch (this.evaluationMode()) {
        case EXTRA:
        case BONUS:
            return this.evaluation();
        case SPELLING:
            if (this.evaluation().compareTo(new BigFraction(10)) >= 0) {
                return BigFraction.ZERO;
            }
            return
                this.weight()
                .divide(weightSum)
                .multiply(new BigFraction(10 - this.evaluation().intValue(), 10))
                .multiply(total);
        default:
            return this.weight().divide(weightSum).multiply(this.evaluation()).multiply(total);
        }
    }

    public String evaluationForDiagram() {
        switch (this.evaluationMode()) {
        case SPELLING:
            if (this.evaluation().compareTo(new BigFraction(10)) >= 0) {
                return "0";
            }
            return String.format(
                Locale.US,
                "%.2f",
                new BigFraction(10 - this.evaluation().intValue(), 10).multiply(10).doubleValue()
            );
        default:
            return String.format(Locale.US, "%.2f", this.evaluation().multiply(10).doubleValue());
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

    public BigFraction weightForSum() {
        switch (this.evaluationMode()) {
        case EXTRA:
        case BONUS:
            return BigFraction.ZERO;
        default:
            return this.weight();
        }
    }

}
