package theseshelper;

import java.io.*;
import java.nio.file.*;
import java.time.*;
import java.util.*;

public abstract class PointsWriter {

    public static void writePoints(final File root, final int year) throws IOException {
        final Points points = PointsWriter.countPoints(root, year);
        final File file = root.toPath().resolve("points" + year + ".txt").toFile();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Erstbetreuung Bachelorarbeiten: ");
            writer.write(String.valueOf(points.bachelorFirst.size()));
            writer.write("\nZweitgutachten Bachelorarbeiten (lang): ");
            writer.write(String.valueOf(points.bachelorSecondLong.size()));
            writer.write("\nZweitgutachten Bachelorarbeiten (kurz): ");
            writer.write(String.valueOf(points.bachelorSecondShort.size()));
            writer.write("\nErstbetreuung Masterarbeiten: ");
            writer.write(String.valueOf(points.masterFirst.size()));
            writer.write("\nZweitgutachten Masterarbeiten (lang): ");
            writer.write(String.valueOf(points.masterSecondLong.size()));
            writer.write("\nZweitgutachten Masterarbeiten (kurz): ");
            writer.write(String.valueOf(points.masterSecondShort.size()));
            writer.write("\nErstbetreuung Praxisarbeiten: ");
            writer.write(String.valueOf(points.practicalThesesFirst.size()));
            writer.write("\nZweitgutachten Praxisarbeiten (lang): ");
            writer.write(String.valueOf(points.practicalThesesSecondLong.size()));
            writer.write("\nZweitgutachten Praxisarbeiten (kurz): ");
            writer.write(String.valueOf(points.practicalThesesSecondShort.size()));
            writer.write("\nPraxischecks: ");
            writer.write(String.valueOf(points.practicalCheck.size()));
            writer.write("\n\nSumme: ");
            writer.write(String.valueOf(points.sum()));
            writer.write("\n\n\n");
            writer.write("Details:\n\n");
            PointsWriter.writeTheses("Erstbetreuung Bachelorarbeiten", points.bachelorFirst, writer);
            PointsWriter.writeTheses("Zweitgutachten Bachelorarbeiten (lang)", points.bachelorSecondLong, writer);
            PointsWriter.writeTheses("Zweitgutachten Bachelorarbeiten (kurz)", points.bachelorSecondShort, writer);
            PointsWriter.writeTheses("Erstbetreuung Masterarbeiten", points.masterFirst, writer);
            PointsWriter.writeTheses("Zweitgutachten Masterarbeiten (lang)", points.masterSecondLong, writer);
            PointsWriter.writeTheses("Zweitgutachten Masterarbeiten (kurz)", points.masterSecondShort, writer);
            PointsWriter.writeTheses("Erstbetreuung Praxisarbeiten", points.practicalThesesFirst, writer);
            PointsWriter.writeTheses("Zweitgutachten Praxisarbeiten (lang)", points.practicalThesesSecondLong, writer);
            PointsWriter.writeTheses("Zweitgutachten Praxisarbeiten (kurz)", points.practicalThesesSecondShort, writer);
            writer.write("Praxischecks:\n");
            for (final LocalDate date : points.practicalCheck) {
                writer.write(date.toString());
                writer.write("\n");
            }
            writer.write("\n");
        }
    }

    private static Points countPoints(final File root, final int year) throws IOException {
        final Points points = new Points();
        final Path theses = root.toPath().resolve("Abschlussarbeiten");
        final Path first = theses.resolve(ResultFileFinder.FIRST);
        final Path second = theses.resolve(ResultFileFinder.SECOND);
        points.bachelorFirst =
            PointsWriter.toResults(ResultFileFinder.findResultFiles(first.resolve(ResultFileFinder.BACHELOR), year));
        final List<Result> bachelorSecond =
            PointsWriter.toResults(ResultFileFinder.findResultFiles(second.resolve(ResultFileFinder.BACHELOR), year));
        points.bachelorSecondLong = bachelorSecond.stream().filter(Result::isLongReviewSet).toList();
        points.bachelorSecondShort = bachelorSecond.stream().filter(Result::isLongReviewNotSet).toList();
        points.masterFirst =
            PointsWriter.toResults(ResultFileFinder.findResultFiles(first.resolve(ResultFileFinder.MASTER), year));
        final List<Result> masterSecond =
            PointsWriter.toResults(ResultFileFinder.findResultFiles(second.resolve(ResultFileFinder.MASTER), year));
        points.masterSecondLong = masterSecond.stream().filter(Result::isLongReviewSet).toList();
        points.masterSecondShort = masterSecond.stream().filter(Result::isLongReviewNotSet).toList();
        points.practicalThesesFirst =
            PointsWriter.toResults(ResultFileFinder.findResultFiles(first.resolve(ResultFileFinder.PA), year));
        final List<Result> paSecond =
            PointsWriter.toResults(ResultFileFinder.findResultFiles(second.resolve(ResultFileFinder.PA), year));
        points.practicalThesesSecondLong = paSecond.stream().filter(Result::isLongReviewSet).toList();
        points.practicalThesesSecondShort = paSecond.stream().filter(Result::isLongReviewNotSet).toList();
        points.practicalCheck =
            Files
            .list(root.toPath().resolve("Vorlesungen").resolve("Praxischeck").resolve("classes"))
            .filter(path -> path.getFileName().toString().startsWith(String.valueOf(year - 2000)))
            .map(
                path -> {
                    Path file;
                    try {
                        file =
                            Files
                            .list(path)
                            .filter(f -> f.getFileName().toString().endsWith(".txt"))
                            .findFirst()
                            .get();
                    } catch (final IOException e) {
                        throw new IllegalStateException(e);
                    }
                    final String name = file.getFileName().toString();
                    final String date = name.substring(0, name.length() - 4);
                    return LocalDate.parse(
                        String.format(
                            "20%s-%s-%s",
                            date.substring(0, 2),
                            date.substring(2, 4),
                            date.substring(4, 6)
                        )
                    );
                }
            ).toList();
        return points;
    }

    private static List<Result> toResults(final List<File> files) {
        return files.stream()
            .map(file -> {
                try {
                    return Result.create(file);
                } catch (final IOException e) {
                    throw new IllegalStateException(e);
                }
            }).toList();
    }

    private static void writeTheses(final String section, final List<Result> results, final BufferedWriter writer)
    throws IOException {
        writer.write(section);
        writer.write(":\n");
        for (final Result result : results) {
            writer.write(result.givennames());
            writer.write(" ");
            writer.write(result.familynames());
            writer.write(": ");
            writer.write(result.title());
            writer.write("\n");
        }
        writer.write("\n");
    }

}
