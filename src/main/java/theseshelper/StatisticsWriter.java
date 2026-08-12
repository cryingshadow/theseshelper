package theseshelper;

import java.io.*;
import java.util.*;

public abstract class StatisticsWriter {

    private static final double[] GRADES = new double[] {1.0, 1.3, 1.7, 2.0, 2.3, 2.7, 3.0, 3.3, 3.7, 4.0, 5.0};

    private static final String STATISTICS = "Statistik";

    private static final String STATISTICS_FILE = "statistics%s%s%s.tex";

    public static void statistics(
        final File root,
        final ReviewerType reviewer,
        final ThesisType type,
        final List<Integer> years
    ) throws IOException {
        for (final int currentYear : years) {
            StatisticsWriter.writeStatistics(
                StatisticsWriter.getTitle(reviewer, type),
                currentYear,
                StatisticsWriter.countGrades(root, currentYear, reviewer, type),
                root
                .toPath()
                .resolve(StatisticsWriter.STATISTICS)
                .resolve(StatisticsWriter.toStatisticsFileName(reviewer, type, currentYear))
                .toFile()
            );
        }
    }

    private static int[] countGrades(
        final File root,
        final int currentYear,
        final ReviewerType reviewer,
        final ThesisType type
    ) throws IOException {
        final List<File> files = new LinkedList<File>();
        switch (reviewer) {
        case ALL:
            files.addAll(
                ResultFileFinder.findResultFiles(root.toPath().resolve(ResultFileFinder.FIRST), type, currentYear)
            );
            files.addAll(
                ResultFileFinder.findResultFiles(root.toPath().resolve(ResultFileFinder.SECOND), type, currentYear)
            );
            break;
        case FIRST:
            files.addAll(
                ResultFileFinder.findResultFiles(root.toPath().resolve(ResultFileFinder.FIRST), type, currentYear)
            );
            break;
        case SECOND:
            files.addAll(
                ResultFileFinder.findResultFiles(root.toPath().resolve(ResultFileFinder.SECOND), type, currentYear)
            );
            break;
        default:
            throw new IllegalStateException("Unknown Selection occurred!");
        }
        return StatisticsWriter.countGrades(files);
    }

    private static int[] countGrades(final List<File> files) throws IOException {
        final int[] result = new int[StatisticsWriter.GRADES.length];
        for (final File file : files) {
            final Result resultFile = Result.create(file);
            if (resultFile.optionalThesisGrade().isEmpty() || resultFile.thesisgrade().isBlank()) {
                continue;
            }
            switch (resultFile.thesisgrade()) {
            case "1,0":
                result[0]++;
                break;
            case "1,3":
                result[1]++;
                break;
            case "1,7":
                result[2]++;
                break;
            case "2,0":
                result[3]++;
                break;
            case "2,3":
                result[4]++;
                break;
            case "2,7":
                result[5]++;
                break;
            case "3,0":
                result[6]++;
                break;
            case "3,3":
                result[7]++;
                break;
            case "3,7":
                result[8]++;
                break;
            case "4,0":
                result[9]++;
                break;
            case "5,0":
                result[10]++;
                break;
            default:
                throw new IOException("Could not parse grade " + resultFile.thesisgrade() + "!");
            }
        }
        return result;
    }

    private static String formatYearForStatistics(final int year) {
        return year > 0 ? String.valueOf(year) : "seit 2022";
    }

    private static String getTitle(final ReviewerType reviewer, final ThesisType type) {
        return String.format("%s mit %s", type.title, reviewer.title);
    }

    private static String toStatisticsFileName(final ReviewerType reviewer, final ThesisType type, final int year) {
        return String.format(
            StatisticsWriter.STATISTICS_FILE,
            reviewer.name().charAt(0) + reviewer.name().substring(1).toLowerCase(),
            type.name(),
            StatisticsWriter.toStatisticsFileNamePart(year)
        );
    }

    private static String toStatisticsFileNamePart(final int year) {
        return year > 0 ? String.valueOf(year) : "AllTime";
    }

    private static void writeStatistics(
        final String title,
        final int year,
        final int[] gradeCount,
        final File file
    ) throws IOException {
        final int maxCount = Math.max(Arrays.stream(gradeCount).max().orElse(0) + 1, 6);
        double average = 0;
        int count = 0;
        for (int i = 0; i < gradeCount.length; i++) {
            count += gradeCount[i];
            average += StatisticsWriter.GRADES[i] * gradeCount[i];
        }
        average = average / count;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("\\documentclass[12pt]{article}\n\n");
            writer.write("\\usepackage[a4paper,landscape,margin=1cm]{geometry}\n");
            writer.write("\\usepackage{pgfplots}\n\n");
            writer.write("\\pagestyle{empty}\n\n");
            writer.write("\\begin{document}\n\n");
            writer.write("\\pgfplotstableread[row sep=\\\\,col sep=&]{\n");
            writer.write("    grade & number \\\\\n");
            writer.write(String.format("    1.0   & %d \\\\\n", gradeCount[0]));
            writer.write(String.format("    1.3   & %d \\\\\n", gradeCount[1]));
            writer.write(String.format("    1.7   & %d \\\\\n", gradeCount[2]));
            writer.write(String.format("    2.0   & %d \\\\\n", gradeCount[3]));
            writer.write(String.format("    2.3   & %d \\\\\n", gradeCount[4]));
            writer.write(String.format("    2.7   & %d \\\\\n", gradeCount[5]));
            writer.write(String.format("    3.0   & %d \\\\\n", gradeCount[6]));
            writer.write(String.format("    3.3   & %d \\\\\n", gradeCount[7]));
            writer.write(String.format("    3.7   & %d \\\\\n", gradeCount[8]));
            writer.write(String.format("    4.0   & %d \\\\\n", gradeCount[9]));
            writer.write(String.format("    5.0   & %d \\\\\n", gradeCount[10]));
            writer.write("    }\\mydata\n\n");
            writer.write("\\vspace*{2cm}\n\n");
            writer.write("\\begin{center}\n\n");
            writer.write(
                String.format(
                    "{\\Huge \\textbf{Notenspiegel %s Ströder %s}}\n\n",
                    title,
                    StatisticsWriter.formatYearForStatistics(year)
                )
            );
            writer.write("\\vspace*{1cm}\n\n");
            writer.write("{\\large\n");
            writer.write("\\begin{tikzpicture}\n");
            writer.write("    \\begin{axis}[\n");
            writer.write("            ybar,\n");
            writer.write("            bar width=.5cm,\n");
            writer.write("            width=0.8\\paperwidth,\n");
            writer.write("            height=0.5\\paperheight,\n");
            writer.write("            legend style={at={(0.5,1)},\n");
            writer.write("                anchor=north,legend columns=-1},\n");
            writer.write("            symbolic x coords={1.0,1.3,1.7,2.0,2.3,2.7,3.0,3.3,3.7,4.0,5.0},\n");
            writer.write("            xtick=data,\n");
            writer.write("            nodes near coords,\n");
            writer.write("            nodes near coords align={vertical},\n");
            writer.write(String.format("            ymin=0,ymax=%d,\n", maxCount));
            writer.write("            ylabel={Anzahl},\n");
            writer.write("            x label style={at={(axis description cs:0.5,-0.05)},anchor=north},\n");
            writer.write("            xlabel={Note}\n");
            writer.write("        ]\n");
            writer.write("        \\addplot table[x=grade,y=number]{\\mydata};\n");
            writer.write("    \\end{axis}\n");
            writer.write("\\end{tikzpicture}\n");
            writer.write("}\n\n");
            writer.write("\\vspace*{8mm}\n\n");
            writer.write(String.format(Locale.GERMAN, "Notendurchschnitt: %.1f\n\n", average));
            writer.write(String.format(Locale.GERMAN, "$n = %d$\n\n", count));
            writer.write("\\end{center}\n\n");
            writer.write("\\end{document}\n");
        }
    }

}
