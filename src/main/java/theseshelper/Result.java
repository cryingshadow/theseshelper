package theseshelper;

import java.io.*;
import java.text.*;
import java.util.*;

import com.google.gson.*;

public record Result(
    Integer points,
    String grade,
    String name,
    String title,
    String otherReviewer,
    String due,
    String location,
    Boolean longReview
) {

    public static final DateFormat FORMAT = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static Result create(final File resultFile) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(resultFile))) {
            return Result.GSON.fromJson(reader, Result.class);
        }
    }

    public Optional<String> optionalOtherReviewer() {
        return Optional.ofNullable(this.otherReviewer());
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

    public Date dueDate() {
        try {
            return Result.FORMAT.parse(this.due());
        } catch (final ParseException e) {
            throw new IllegalStateException(String.format("Date parsing failed for \"%s\"!", this.due()), e);
        }
    }

    public Optional<Integer> optionalPoints() {
        if (this.points() == null || this.points() < 0) {
            return Optional.empty();
        }
        return Optional.of(this.points());
    }

    public void write(final BufferedWriter writer) throws IOException {
        Result.GSON.toJson(this, this.getClass(), writer);
    }

    public Result setOtherReviewer(final String otherReviewer) {
        return new Result(
            this.points(),
            this.grade(),
            this.name(),
            this.title(),
            otherReviewer,
            this.due(),
            this.location(),
            this.longReview()
        );
    }

}
