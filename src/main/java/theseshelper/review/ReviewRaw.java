package theseshelper.review;

import java.io.*;
import java.util.*;

import theseshelper.*;

public record ReviewRaw(
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
    Boolean tworeviewers,
    String otherreviewer,
    String goal,
    List<String> contributions,
    List<ReviewEvaluationGroupRaw> evaluationgroups,
    String criteriapath,
    Boolean pagebreaktotal,
    String bonusstart,
    String bonus,
    String totalstart,
    String alternativetotaltext,
    String additionaltotaltext,
    String totalexpected,
    Boolean corner
) {

    public Review toReview() {
        return new Review(
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
            this.tworeviewers(),
            this.otherreviewer(),
            this.goal(),
            this.contributions(),
            this.evaluationgroups() == null ?
                null :
                    this.evaluationgroups().stream().map(ReviewEvaluationGroupRaw::toEvaluationGroup).toList(),
            this.criteriapath(),
            this.pagebreaktotal(),
            this.bonusstart(),
            Review.parseRationalNumber(this.bonus()),
            this.totalstart(),
            this.alternativetotaltext(),
            this.additionaltotaltext(),
            Review.parseRationalNumber(this.totalexpected()),
            this.corner()
        );
    }

    public void write(final BufferedWriter writer) throws IOException {
        Main.GSON.toJson(this, this.getClass(), writer);
    }

}
