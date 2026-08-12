package theseshelper;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public abstract class ResultFileFinder {

    public static final String BACHELOR = "Bachelor";

    public static final String FIRST = "Erstgutachten";

    public static final String MASTER = "Master";

    public static final String PA = "Praxisarbeiten";

    public static final String RESULT = "result.json";

    public static final String SECOND = "Zweitgutachten";

    public static List<File> findAllResultFiles(final File root, final int year) throws IOException {
        final Path rootPath = root.toPath();
        final List<File> files =
            ResultFileFinder.findResultFiles(rootPath.resolve(ResultFileFinder.FIRST), ThesisType.ALL, year);
        files.addAll(ResultFileFinder.findResultFiles(rootPath.resolve(ResultFileFinder.SECOND), ThesisType.ALL, year));
        return files;
    }

    public static List<File> findResultFiles(final Path thesisTypePath, final int year) throws IOException {
        final String yearString = String.valueOf(year);
        Main.LOGGER.log(Level.FINE, "Finding result files in: " + thesisTypePath.toString());
        return Files
            .list(thesisTypePath)
            .filter(p -> p.getFileName().toString().startsWith(yearString))
            .map(p -> p.resolve(ResultFileFinder.RESULT))
            .filter(p -> Files.exists(p))
            .map(Path::toFile)
            .toList();
    }

    public static List<File> findResultFiles(
        final Path reviewerTypePath,
        final ThesisType type,
        final int year
    ) throws IOException {
        final List<File> result = new LinkedList<File>();
        switch (type) {
        case BA:
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.BACHELOR), year));
            break;
        case MA:
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.MASTER), year));
            break;
        case PA:
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.PA), year));
            break;
        case ALL_BUT_PA:
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.BACHELOR), year));
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.MASTER), year));
            break;
        default:
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.BACHELOR), year));
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.MASTER), year));
            result.addAll(ResultFileFinder.findResultFiles(reviewerTypePath.resolve(ResultFileFinder.PA), year));
        }
        return result;
    }

}
