package theseshelper.review;

public record CriterionTextRaw(String achieved, String text) {

    public CriterionText toCriterionText() {
        return new CriterionText(Review.parseRationalNumber(this.achieved()), this.text());
    }

}
