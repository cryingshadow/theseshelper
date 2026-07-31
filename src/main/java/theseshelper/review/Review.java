package theseshelper.review;

import java.util.*;

import org.apache.commons.math3.fraction.*;

import theseshelper.*;

public record Review(
    ThesisType type,
    String title,
    String student,
    String date,
    String place,
    String reviewer,
    String signature,
    Boolean restricted,
    Boolean twoReviewers,
    String otherReviewer,
    List<String> contributions,
    List<ReviewEvaluationGroup> evaluationGroups,
    String criteriaPath,
    Boolean pagebreakTotal,
    String bonusStart,
    ReviewEvaluation bonus,
    String totalStart,
    String alternativeTotalText,
    String additionalTotalText,
    BigFraction totalExpected
) {

    public static BigFraction parseRationalNumber(final String number) {
        if (number == null || number.isBlank()) {
            return null;
        }
        if (number.contains(".")) {
            final String[] parts = number.split("\\.", -1);
            if (parts.length != 2) {
                throw new NumberFormatException(String.format("Number %s contains more than one dot!", number));
            }
            final int exponent = parts[1].length();
            final int denominator = Integer.parseInt("1" + "0".repeat(exponent));
            final int beforeComma = parts[0].length() == 0 ? 0 : Integer.parseInt(parts[0]);
            final int afterComma = parts[1].length() == 0 ? 0 : Integer.parseInt(parts[1]);
            return new BigFraction(beforeComma * denominator + afterComma, denominator);
        }
        final String[] parts = number.split("/");
        if (parts.length > 2) {
            throw new NumberFormatException(String.format("Number %s contains more than one slash!", number));
        }
        return parts.length == 1 ?
            new BigFraction(Integer.parseInt(parts[0].strip())) :
                new BigFraction(Integer.parseInt(parts[0].strip()), Integer.parseInt(parts[1].strip()));
    }

    public BigFraction weightSum() {
        return
            this.evaluationGroups()
            .stream()
            .map(ReviewEvaluationGroup::weight)
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y));
    }

    public BigFraction evaluate() {
        return
            this.evaluationGroups()
            .stream()
            .map(g -> g.evaluate(this.totalExpected(), this.weightSum()))
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y));
    }

    public boolean hasUnusedCriterion() {
        return this.evaluationGroups().stream().anyMatch(ReviewEvaluationGroup::hasUnusedCriterion);
    }

}
