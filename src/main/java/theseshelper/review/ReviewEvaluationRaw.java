package theseshelper.review;

public record ReviewEvaluationRaw(
    String criterion,
    String evaluation,
    String weight,
    String alternative,
    String additional,
    EvaluationMode mode
) {

    public ReviewEvaluation toEvaluation() {
        return new ReviewEvaluation(
            this.criterion(),
            Review.parseRationalNumber(this.evaluation()),
            Review.parseRationalNumber(this.weight()),
            this.alternative(),
            this.additional(),
            this.mode()
        );
    }

}
