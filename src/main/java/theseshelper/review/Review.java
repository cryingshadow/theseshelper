package theseshelper.review;

import java.io.*;
import java.util.*;

import org.apache.commons.math3.fraction.*;

import theseshelper.*;

public record Review(
    Boolean empty,
    String version,
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
    String goal,
    List<String> contributions,
    List<ReviewEvaluationGroup> evaluationGroups,
    String criteriaPath,
    Boolean pagebreakTotal,
    String bonusStart,
    BigFraction bonus,
    String totalStart,
    String alternativeTotalText,
    String additionalTotalText,
    BigFraction totalExpected
) {

    public static Review parse(final File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            return Main.GSON.fromJson(reader, ReviewRaw.class).toReview();
        }
    }

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

    public BigFraction evaluate() {
        return
            this.evaluationGroups()
            .stream()
            .map(g -> g.evaluate(this.totalExpected(), this.weightSum()))
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y))
            .add(this.bonus() == null ? BigFraction.ZERO : this.bonus());
    }

    public boolean hasUnusedCriterion() {
        return this.evaluationGroups().stream().anyMatch(ReviewEvaluationGroup::hasUnusedCriterion);
    }

    public boolean isOlderVersion(final String version) {
        final String type = version.substring(0, 2);
        final String currentVersion = this.version();
        final String currentType = currentVersion.substring(0, 2);
        if (!type.equals(currentType)) {
            return false;
        }
        final String[] versionNumber = version.substring(3).split("\\.");
        final String[] currentVersionNumber = currentVersion.substring(3).split("\\.");
        int i = 0;
        while (i < versionNumber.length && i < currentVersionNumber.length) {
            final int compare =
                Integer.compare(Integer.parseInt(versionNumber[i]), Integer.parseInt(currentVersionNumber[i]));
            if (compare < 0) {
                return true;
            } else if (compare > 0) {
                return false;
            }
            i++;
        }
        if (versionNumber.length < currentVersionNumber.length) {
            while (i < currentVersionNumber.length) {
                if (Integer.parseInt(currentVersionNumber[i]) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public ReviewRaw toRaw() {
        return new ReviewRaw(
            this.empty(),
            this.version(),
            this.type(),
            this.title(),
            this.student(),
            this.date(),
            this.place(),
            this.reviewer(),
            this.signature(),
            this.restricted(),
            this.twoReviewers(),
            this.otherReviewer(),
            this.goal(),
            this.contributions(),
            this.evaluationGroups() == null ?
                List.of() :
                    this.evaluationGroups().stream().map(ReviewEvaluationGroup::toRaw).toList(),
            this.criteriaPath(),
            this.pagebreakTotal(),
            this.bonusStart(),
            this.bonus() == null ? null : this.bonus().toString(),
            this.totalStart(),
            this.alternativeTotalText(),
            this.additionalTotalText(),
            this.totalExpected() == null ? "" : this.totalExpected().toString()
        );
    }

    public BigFraction weightSum() {
        return
            this.evaluationGroups()
            .stream()
            .map(ReviewEvaluationGroup::weight)
            .reduce(BigFraction.ZERO, (x, y) -> x.add(y));
    }

}
