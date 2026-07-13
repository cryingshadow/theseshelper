package thesesstats;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;

public class UnfinishedSubmissions extends ArrayList<TopicSubmission> {

    private static final long serialVersionUID = 1L;

    private static boolean containsPDF(final File resultFile) {
        return Arrays.stream(resultFile.getParentFile().list()).anyMatch(file -> file.toLowerCase().matches(".*pdf"));
    }

    public UnfinishedSubmissions(final File root, final List<Integer> years) throws IOException {
        for (final int currentYear : years) {
            for (final File resultFile : Main.findAllResultFiles(root, currentYear)) {
                this.processResultFile(resultFile);
            }
        }
        Collections.sort(this);
    }

    private UnfinishedSubmissions() {
        super();
    }

    public void write() {
        final Map<String, Integer> registered = new TreeMap<String, Integer>();
        final Map<String, Integer> submitted = new TreeMap<String, Integer>();
        final Map<String, Map<String, UnfinishedSubmissions>> colloquia =
            new TreeMap<String, Map<String, UnfinishedSubmissions>>();
        for (final TopicSubmission submission : this) {
            switch (submission.type()) {
            case "Bachelor":
            case "Bachelor2":
            case "Master":
            case "Master2":
                if (!colloquia.containsKey(submission.location())) {
                    colloquia.put(submission.location(), new TreeMap<String, UnfinishedSubmissions>());
                }
                final Map<String, UnfinishedSubmissions> atLocation = colloquia.get(submission.location());
                final String otherExaminer = submission.otherExaminer().orElse("");
                if (!atLocation.containsKey(otherExaminer)) {
                    atLocation.put(otherExaminer, new UnfinishedSubmissions());
                }
                atLocation.get(otherExaminer).add(submission);
            }
            System.out.print(Result.FORMAT.format(submission.due()));
            System.out.print(" (");
            if (submission.submitted()) {
                submitted.merge(submission.type(), 1, Integer::sum);
                System.out.print("SUBMITTED");
            } else {
                registered.merge(submission.type(), 1, Integer::sum);
                System.out.print("REGISTERED");
            }
            System.out.print("): ");
            System.out.print(submission.student());
            System.out.print(" (");
            System.out.print(submission.type());
            System.out.println(")");
        }
        System.out.println();
        System.out.println("Colloquia:");
        if (colloquia.isEmpty()) {
            System.out.println("none");
        } else {
            for (final Map.Entry<String, Map<String, UnfinishedSubmissions>> locationEntry : colloquia.entrySet()) {
                System.out.print(locationEntry.getKey());
                System.out.println(":");
                for (
                    final Map.Entry<String, UnfinishedSubmissions> reviewerEntry :
                        locationEntry.getValue().entrySet()
                ) {
                    System.out.print("  ");
                    System.out.print(reviewerEntry.getKey());
                    System.out.println(":");
                    for (final TopicSubmission submission : reviewerEntry.getValue()) {
                        System.out.print("    ");
                        System.out.println(submission.student());
                    }
                    System.out.println("    Total: " + reviewerEntry.getValue().size());
                }
            }
        }
        System.out.println();
        System.out.println("Statistics:");
        for (final Map.Entry<String, Integer> entry : registered.entrySet()) {
            System.out.println(String.format("Registered %s: %d", entry.getKey(), entry.getValue()));
        }
        for (final Map.Entry<String, Integer> entry : submitted.entrySet()) {
            System.out.println(String.format("Submitted %s: %d", entry.getKey(), entry.getValue()));
        }
        System.out.println(
            String.format(
                "Total: %d",
                registered.values().stream().mapToInt(Integer::intValue).sum()
                + submitted.values().stream().mapToInt(Integer::intValue).sum()
            )
        );
    }

    private void processResultFile(final File resultFile) throws IOException {
        Main.LOGGER.log(Level.FINEST, "Checking result file: " + resultFile.toString());
        final Result result = Result.create(resultFile);
        if (result.optionalPoints().isEmpty()) {
            try {
                final Path grandparentPath = resultFile.toPath().getParent().getParent();
                final boolean secondReviewer =
                    "Zweitgutachten".equals(grandparentPath.getParent().getFileName().toString());
                this.add(
                    new TopicSubmission(
                        grandparentPath.getFileName().toString() + (secondReviewer ? "2" : ""),
                        result.name(),
                        result.dueDate(),
                        UnfinishedSubmissions.containsPDF(resultFile),
                        result.optionalOtherReviewer(),
                        !secondReviewer,
                        result.optionalLocation().orElse("")
                    )
                );
            } catch (IllegalStateException | IndexOutOfBoundsException e) {
                throw new IOException(result.name() + ", " + result.title(), e);
            }
        }
        Main.LOGGER.log(Level.FINEST, "Check done!");
    }

}
