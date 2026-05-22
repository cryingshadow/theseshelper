package thesesstats;

import java.io.*;
import java.nio.file.*;
import java.text.*;
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
                Main.LOGGER.log(Level.FINEST, "Checking result file: " + resultFile.toString());
                if (Files.lines(resultFile.toPath()).findFirst().get().isBlank()) {
                    final List<String> data = Files.lines(resultFile.toPath()).skip(2).toList();
                    try {
                        final Path grandparentPath = resultFile.toPath().getParent().getParent();
                        this.add(
                            new TopicSubmission(
                                grandparentPath.getFileName().toString()
                                + ("Zweitgutachten".equals(
                                    grandparentPath.getParent().getFileName().toString()) ? "2" : ""
                                ),
                                data.get(0),
                                TopicSubmission.FORMAT.parse(data.get(3)),
                                UnfinishedSubmissions.containsPDF(resultFile)
                            )
                        );
                    } catch (ParseException | IndexOutOfBoundsException e) {
                        throw new IOException(data.get(0) + ", " + data.get(1), e);
                    }
                }
                Main.LOGGER.log(Level.FINEST, "Check done!");
            }
        }
        Collections.sort(this);
    }

    public void write() {
        int registered = 0;
        int submitted = 0;
        for (final TopicSubmission submission : this) {
            System.out.print(TopicSubmission.FORMAT.format(submission.due()));
            System.out.print(" (");
            if (submission.submitted()) {
                submitted++;
                System.out.print("SUBMITTED");
            } else {
                registered++;
                System.out.print("REGISTERED");
            }
            System.out.print("): ");
            System.out.print(submission.student());
            System.out.print(" (");
            System.out.print(submission.type());
            System.out.println(")");
        }
        System.out.println(
            String.format("Total: %d (%d registered, %d submitted)", registered + submitted, registered, submitted)
        );
    }

}
