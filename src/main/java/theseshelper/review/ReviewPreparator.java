package theseshelper.review;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

import theseshelper.*;

public abstract class ReviewPreparator {

    private static final String COMPILE = "compile.sh";

    private static final String FINISH = "finish.sh";

    private static final String TEXT = "text.txt";

    public static void prepare(final File root, final int year) throws IOException, InterruptedException {
        for (final File resultFile : ResultFileFinder.findAllResultFiles(root, year)) {
            Main.LOGGER.log(Level.FINE, "Preparing result file: " + resultFile.toString());
            ReviewPreparator.createTextFileIfNotExists(resultFile);
            ReviewPreparator.createOrUpdateReviewFiles(resultFile, year);
        }
    }

    private static void createCompileFilesIfNotExist(final File jsonFile) throws IOException {
        final File compileFile = jsonFile.toPath().getParent().resolve(ReviewPreparator.COMPILE).toFile();
        final File finishFile = jsonFile.toPath().getParent().resolve(ReviewPreparator.FINISH).toFile();
        if (!compileFile.exists()) {
            final String name = jsonFile.getName();
            final String pdflatex = "pdflatex " + name.substring(0, name.length() - 5);
            final String pdflatexWithComments = pdflatex + "mitKommentaren";
            ReviewPreparator.writeScriptFile(
                List.of(
                    "java -jar ../../../../theseshelper.jar -m REVIEW -i " + name,
                    "",
                    pdflatex,
                    pdflatex
                ),
                compileFile
            );
            ReviewPreparator.writeScriptFile(
                List.of(
                    "java -jar ../../../../theseshelper.jar -m FINISH -i " + name,
                    "",
                    pdflatexWithComments,
                    pdflatexWithComments
                ),
                finishFile
            );
        }
    }

    private static void createOrUpdateReviewFiles(final File resultFile, final int year) throws IOException {
        final ThesisType thesisType = ThesisType.fromFile(resultFile);
        final Path directory = resultFile.getAbsoluteFile().toPath().getParent();
        final Result fileContent = Result.create(resultFile);
        if (
            fileContent.title().isBlank()
            || (fileContent.thesisgrade() != null && !fileContent.thesisgrade().isBlank())
        ) {
            return;
        }
        final String prefix =
            String.format(
                "gutachten%d%s%s%s",
                year,
                fileContent.familynames().replaceAll(" ", ""),
                fileContent.givennames().replaceAll(" ", ""),
                thesisType.name()
            );
        final File reviewFile = directory.resolve(String.format("%s.json", prefix)).toFile();
        ReviewPreparator.createCompileFilesIfNotExist(reviewFile);
        final Review template = ReviewTemplate.selectReviewTemplate(thesisType, fileContent);
        if (reviewFile.exists()) {
            final Review review = Review.parse(reviewFile);
            if (ReviewPreparator.isEmptyAndOlderVersion(review, template)) {
                Main.LOGGER.log(Level.FINE, "Updating templates for result file: " + resultFile.toString());
            } else {
                return;
            }
        } else {
            Main.LOGGER.log(Level.FINE, "Creating templates for result file: " + resultFile.toString());
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reviewFile, Main.UTF8))) {
            template.toRaw().write(writer);
        }
        try (
            BufferedWriter writer =
                new BufferedWriter(
                    new FileWriter(directory.resolve(String.format("%smitKommentaren.tex", prefix)).toFile(), Main.UTF8)
                )
        ) {
            writer.write("\\documentclass{article}\n\n");
            writer.write("\\usepackage{pdfpages}\n\n");
            writer.write("\\pagestyle{empty}\n\n");
            writer.write("\\begin{document}\n\n");
            writer.write(String.format("\\includepdf[pages=-,fitpaper]{%s.pdf}\n\n", prefix));
            writer.write("\\pagebreak\n\n");
            writer.write("\\includepdf[pages=-,fitpaper]{thesisWithComments.pdf}\n\n");
            writer.write("\\end{document}\n");
        }
    }

    private static void createTextFileIfNotExists(final File resultFile) throws IOException, InterruptedException {
        if (Arrays.stream(resultFile.getParentFile().list()).filter(name -> name.endsWith(".pdf")).count() != 1) {
            return;
        }
        final File pdf =
            Arrays
            .stream(resultFile.getParentFile().listFiles())
            .filter(file -> file.getName().endsWith(".pdf"))
            .findAny()
            .get();
        final File directory = resultFile.getParentFile().getAbsoluteFile();
        final File textFile = directory.toPath().resolve(ReviewPreparator.TEXT).toFile();
        if (
            !textFile.exists()
            && new ProcessBuilder()
            .directory(directory)
            .command("cmd.exe", "/c", String.format("pdftotext \"%s\" text.txt", pdf.getName().replaceAll(" ", "\\ ")))
            .start()
            .waitFor() != 0
        ) {
            throw new IOException(
                String.format(
                    "Non-zero exit code! Command: pdftotext, Directory: %s, File: %s",
                    directory.toString(),
                    pdf.getName()
                )
            );
        }
    }

    private static boolean isEmptyAndOlderVersion(final Review review, final Review template) throws IOException {
        return review.empty() != null && review.empty() && template.isOlderVersion(review.version());
    }

    private static void writeScriptFile(final List<String> text, final File file) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, Main.UTF8))) {
            writer.write("#!/bin/bash\n\n");
            for (final String line : text) {
                writer.write(line);
                writer.write("\n");
            }
        }
        file.setExecutable(true);
    }

}
