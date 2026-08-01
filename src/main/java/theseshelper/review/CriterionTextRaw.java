package theseshelper.review;

public record CriterionTextRaw(String achieved, String text) implements Comparable<CriterionTextRaw> {

    @Override
    public int compareTo(final CriterionTextRaw o) {
        return this.toCriterionText().compareTo(o.toCriterionText());
    }

    public CriterionText toCriterionText() {
        return new CriterionText(Review.parseRationalNumber(this.achieved()), this.text());
    }

}
