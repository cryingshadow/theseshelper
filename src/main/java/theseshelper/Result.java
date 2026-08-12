package theseshelper;

import java.io.*;
import java.text.*;
import java.util.*;

public record Result(
    Integer points,
    String thesisgrade,
    String givennames,
    String familynames,
    String title,
    String otherReviewer,
    String due,
    String colloquiumdate,
    String location,
    String colloquiumgrade,
    Boolean longReview
) {

    public static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    public static Result create(final File resultFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile))) {
            return Main.GSON.fromJson(reader, Result.class);
        }
    }

    public Optional<Date> optionalColloquiumDate() {
        if (this.colloquiumdate() == null || this.colloquiumdate().isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Result.FORMAT.parse(this.colloquiumdate()));
        } catch (final ParseException e) {
            throw new IllegalStateException(String.format("Date parsing failed for \"%s\"!", this.colloquiumdate()), e);
        }
    }

    public Date dueDate() {
        try {
            return Result.FORMAT.parse(this.due());
        } catch (final ParseException e) {
            throw new IllegalStateException(String.format("Date parsing failed for \"%s\"!", this.due()), e);
        }
    }

    public boolean isLongReviewNotSet() {
        return this.optionalLongReview().map(value -> !value).orElse(true);
    }

    public boolean isLongReviewSet() {
        return this.optionalLongReview().orElse(false);
    }

    public Optional<String> optionalThesisGrade() {
        return Optional.ofNullable(this.thesisgrade());
    }

    public Optional<String> optionalLocation() {
        return Optional.ofNullable(this.location());
    }

    public Optional<Boolean> optionalLongReview() {
        return Optional.ofNullable(this.longReview());
    }

    public Optional<String> optionalOtherReviewer() {
        return Optional.ofNullable(this.otherReviewer());
    }

    public Optional<Integer> optionalPoints() {
        if (this.points() == null || this.points() < 0) {
            return Optional.empty();
        }
        return Optional.of(this.points());
    }

    public Result setOtherReviewer(final String otherReviewer) {
        return new Result(
            this.points(),
            this.thesisgrade(),
            this.givennames(),
            this.familynames(),
            this.title(),
            otherReviewer,
            this.due(),
            this.colloquiumdate(),
            this.location(),
            this.colloquiumgrade(),
            this.longReview()
        );
    }

    public void write(final File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, Main.UTF8))) {
            Main.GSON.toJson(this, this.getClass(), writer);
        }
    }

}
