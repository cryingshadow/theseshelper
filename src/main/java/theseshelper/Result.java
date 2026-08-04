package theseshelper;

import java.io.*;
import java.text.*;
import java.util.*;

public record Result(
    Integer points,
    String grade,
    String givennames,
    String familynames,
    String title,
    String otherReviewer,
    String due,
    String colloquium,
    String location,
    Boolean longReview
) {

    public static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    public static Result create(final File resultFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile))) {
            return Main.GSON.fromJson(reader, Result.class);
        }
    }

    public Optional<Date> colloquiumDate() {
        if (this.colloquium() == null || this.colloquium().isBlank()) {
            return Optional.empty();
        }
        try {
            return Optional.of(Result.FORMAT.parse(this.colloquium()));
        } catch (final ParseException e) {
            throw new IllegalStateException(String.format("Date parsing failed for \"%s\"!", this.colloquium()), e);
        }
    }

    public Date dueDate() {
        try {
            return Result.FORMAT.parse(this.due());
        } catch (final ParseException e) {
            throw new IllegalStateException(String.format("Date parsing failed for \"%s\"!", this.due()), e);
        }
    }

    public Optional<String> optionalGrade() {
        return Optional.ofNullable(this.grade());
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
            this.grade(),
            this.givennames(),
            this.familynames(),
            this.title(),
            otherReviewer,
            this.due(),
            this.colloquium(),
            this.location(),
            this.longReview()
        );
    }

    public void write(final BufferedWriter writer) throws IOException {
        Main.GSON.toJson(this, this.getClass(), writer);
    }

}
