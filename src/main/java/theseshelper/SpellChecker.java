package theseshelper;

import java.io.*;
import java.util.logging.*;

public abstract class SpellChecker {

    private static final String ERROR = "errors.txt";

    private static final String REDUCED = "reduced.txt";

    public static void spellcheck(final File root, final int year) throws IOException, InterruptedException {
        for (final File resultFile : ResultFileFinder.findAllResultFiles(root, year)) {
            final File directory = resultFile.getParentFile().getAbsoluteFile();
            final File errorsFile = directory.toPath().resolve(SpellChecker.ERROR).toFile();
            final File reducedFile = directory.toPath().resolve(SpellChecker.REDUCED).toFile();
            final String fileName = resultFile.toString();
            Main.LOGGER.log(Level.FINE, "Spell checking result file: " + fileName);
            if (
                !errorsFile.exists()
                && reducedFile.exists()
                && new ProcessBuilder()
                .directory(directory)
                .command(
                    "java",
                    "-jar",
                    "..\\..\\..\\..\\spella.jar",
                    SpellChecker.REDUCED,
                    SpellChecker.ERROR,
                    "..\\..\\..\\..\\..\\templates\\personal.txt"
                    ).start()
                .waitFor() != 0
            ) {
                throw new IOException(
                    String.format("Non-zero exit code! Command: spella, Directory: %s", directory.toString())
                );
            }
            Main.LOGGER.log(Level.FINE, String.format("Spell checking %s done!", fileName));
        }
    }

}
