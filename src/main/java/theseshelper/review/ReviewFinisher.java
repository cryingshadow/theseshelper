package theseshelper.review;

import java.io.*;

import theseshelper.*;

public class ReviewFinisher {

    public static void finish(final File reviewFile) throws IOException {
        final File resultFile =
            reviewFile.toPath().toAbsolutePath().getParent().resolve(ResultFileFinder.RESULT).toFile();
        final Review review = Review.parse(reviewFile);
        final Result result = Result.create(resultFile);
        new Result(
            ReviewEvaluator.evaluate(review),
            ReviewEvaluator.toGrade(review),
            result.givennames(),
            result.familynames(),
            result.title(),
            result.otherReviewer(),
            result.due(),
            result.colloquiumdate(),
            result.location(),
            result.colloquiumgrade(),
            result.longReview()
        ).write(resultFile);
    }

}
