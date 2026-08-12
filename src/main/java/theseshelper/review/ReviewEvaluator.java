package theseshelper.review;

import org.apache.commons.math3.fraction.*;

public abstract class ReviewEvaluator {

    public static int evaluate(final Review review) {
        return
            review.evaluationGroups()
            .stream()
            .map(g -> ReviewEvaluator.evaluate(g, review.totalExpected(), ReviewEvaluator.weightSum(review)).intValue())
            .reduce(0, Integer::sum)
            + (review.bonus() == null ? 0 : review.bonus().intValue());
    }

    public static BigFraction evaluate(
        final ReviewEvaluationGroup group,
        final BigFraction totalExpected,
        final BigFraction weightSum
    ) {
        final BigFraction innerTotal = ReviewEvaluator.total(group, totalExpected, weightSum);
        final BigFraction innerWeightSum =
            group.evaluations()
            .stream()
            .map(ReviewEvaluation::weightForSum)
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y));
        return
            group.evaluations()
            .stream()
            .map(e -> e.evaluate(innerTotal, innerWeightSum))
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y))
            .add(group.adjust() == null ? BigFraction.ZERO : new BigFraction(group.adjust()));
    }

    public static boolean isCornerValue(final Review review) {
        switch (ReviewEvaluator.percentage(review).multiply(100).intValue()) {
        case 47:
        case 48:
        case 49:
        case 58:
        case 66:
        case 71:
        case 76:
        case 80:
        case 84:
        case 88:
        case 91:
        case 96:
            return true;
        default:
            return false;
        }
    }

    public static String toGrade(final Review review) {
        return ReviewEvaluator.toGrade(review, false);
    }

    public static String toGrade(final Review review, final boolean forLaTeX) {
        final BigFraction percent = ReviewEvaluator.percentage(review);
        if (percent.compareTo(new BigFraction(97,100)) >= 0) {
            return forLaTeX ? "1{,}0" : "1,0";
        }
        if (percent.compareTo(new BigFraction(92,100)) >= 0) {
            return forLaTeX ? "1{,}3" : "1,3";
        }
        if (percent.compareTo(new BigFraction(89,100)) >= 0) {
            return forLaTeX ? "1{,}7" : "1,7";
        }
        if (percent.compareTo(new BigFraction(85,100)) >= 0) {
            return forLaTeX ? "2{,}0" : "2,0";
        }
        if (percent.compareTo(new BigFraction(81,100)) >= 0) {
            return forLaTeX ? "2{,}3" : "2,3";
        }
        if (percent.compareTo(new BigFraction(77,100)) >= 0) {
            return forLaTeX ? "2{,}7" : "2,7";
        }
        if (percent.compareTo(new BigFraction(72,100)) >= 0) {
            return forLaTeX ? "3{,}0" : "3,0";
        }
        if (percent.compareTo(new BigFraction(67,100)) >= 0) {
            return forLaTeX ? "3{,}3" : "3,3";
        }
        if (percent.compareTo(new BigFraction(59,100)) >= 0) {
            return forLaTeX ? "3{,}7" : "3,7";
        }
        if (percent.compareTo(new BigFraction(50,100)) >= 0) {
            return forLaTeX ? "4{,}0" : "4,0";
        }
        return forLaTeX ? "5{,}0" : "5,0";
    }

    public static String toGradeForLaTeX(final Review review) {
        return ReviewEvaluator.toGrade(review, true);
    }

    public static BigFraction total(
        final ReviewEvaluationGroup group,
        final BigFraction totalExpected,
        final BigFraction weightSum
    ) {
        return group.weight().divide(weightSum).multiply(totalExpected);
    }

    public static BigFraction weightSum(final Review review) {
        return
            review.evaluationGroups()
            .stream()
            .map(ReviewEvaluationGroup::weight)
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y));
    }

    private static BigFraction percentage(final Review review) {
        return new BigFraction(ReviewEvaluator.evaluate(review)).divide(review.totalExpected());
    }

}
