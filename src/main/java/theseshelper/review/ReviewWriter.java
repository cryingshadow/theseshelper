package theseshelper.review;

import java.io.*;

import org.apache.commons.math3.fraction.*;

import theseshelper.*;

public class ReviewWriter {

    public static File write(final File reviewFile) throws IOException {
        final Review review = Review.parse(reviewFile);
        final Criteria criteria = ReviewWriter.parseCriteria(reviewFile, review);
        final File result = ReviewWriter.toOutputFile(reviewFile);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(result))) {
            writer.write("\\documentclass{article}\n\n");
            writer.write("\\usepackage{fhdwevaluation}\n\n");
            writer.write("\\begin{document}\n\n");
            writer.write("\\begin{center}\n");
            writer.write("{\\Huge \\textbf{Gutachten}}\\\\[5ex]\n");
            writer.write("{\\large über die ");
            writer.write(review.type().title.substring(0, review.type().title.length() - 2));
            writer.write(" von \\textbf{");
            writer.write(review.student());
            writer.write("} mit dem Titel}\\\\[2ex]\n");
            writer.write("\\begin{spacing}{1.8}\n");
            writer.write("{\\LARGE \\textbf{");
            writer.write(review.title());
            writer.write("}}\n");
            writer.write("\\end{spacing}\n\n");
            writer.write("\\vspace*{1ex}\n\n");
            writer.write("{\\large ");
            writer.write(review.reviewer());
            writer.write("\\\\[2ex] Datum: ");
            writer.write(review.date());
            writer.write("}\n");
            writer.write("\\end{center}\n\n");
            writer.write("\\vspace*{2ex}\n\n");
            if (review.restricted()) {
                writer.write("\\begin{center}\n");
                writer.write("\\textcolor{red}{Die Arbeit enthält einen Sperrvermerk, sodass auch dieses ");
                writer.write("Gutachten\\\\entsprechend vertraulich behandelt werden muss.}\n");
                writer.write("\\end{center}\n\n");
                writer.write("\\vspace*{2ex}\n\n");
            }
            final String spellingErrorText = " durch eine automatische Rechtschreibprüfung gefunden ";
            final BigFraction weightSum = review.weightSum();
            for (final ReviewEvaluationGroup group : review.evaluationGroups()) {
                if (group.space() != null) {
                    writer.write(group.space());
                    writer.write("\n\n");
                }
                writer.write("\\section{");
                writer.write(group.title());
                writer.write("}\n");
                if (group.starttext() != null) {
                    writer.write(group.starttext());
                    writer.write("\n");
                }
                for (final ReviewEvaluation evaluation : group.evaluations()) {
                    switch (evaluation.evaluationMode()) {
                    case SPELLING:
                        writer.write("Die Arbeit enthält ");
                        final String errors = evaluation.evaluation().toString();
                        switch (errors) {
                        case "0":
                            writer.write("keine Rechtschreibfehler, welche");
                            writer.write(spellingErrorText);
                            writer.write("wurden.");
                            break;
                        case "1":
                            writer.write("einen Rechtschreibfehler, welcher");
                            writer.write(spellingErrorText);
                            writer.write("wurde.");
                            break;
                        default:
                            writer.write(errors);
                            writer.write(" Rechtschreibfehler, welche");
                            writer.write(spellingErrorText);
                            writer.write("wurden.");
                        }
                        break;
                    default:
                        final CriterionTextSelector selector = criteria.get(evaluation.criterion());
                        writer.write(selector.apply(evaluation.evaluation()));
                        writer.write("\n");
                    }
                }
                writer.write("\\begin{flushright}{Bewertung: ");
                writer.write(String.valueOf(group.evaluate(review.totalExpected(), weightSum).intValue()));
                writer.write(" von ");
                writer.write(group.total(review.totalExpected(), weightSum).toString());
                writer.write(" Punkten}\\end{flushright}\n\n");
            }
            if (review.pagebreakTotal() != null && review.pagebreakTotal()) {
                writer.write("\\pagebreak\n\n");
            }
            writer.write("\\section{Gesamtbeurteilung}\n");
            final int achieved = review.evaluate().intValue();
            writer.write("Insgesamt wurde");
            if (achieved == 1) {
                writer.write(" 1 Punkt");
            } else {
                writer.write("n ");
                writer.write(String.valueOf(achieved));
                writer.write(" Punkte");
            }
            writer.write(" erreicht und das Gesamturteil lautet:\n");
            writer.write("\\begin{center}{\\large\\textbf{");
            writer.write(ReviewWriter.toGrade(new BigFraction(achieved).divide(review.totalExpected())));
            writer.write("}}\\end{center}\n\n");
            if (review.hasUnusedCriterion()) {
                writer.write("\\footnote{\\textcolor{red}{Nicht alle Kriterien wurden bewertet!}}\n\n");
            }
            writer.write("\\vspace*{7ex}\n\n");
            if (review.twoReviewers()) {
                writer.write("\\begin{center}\n");
                writer.write("\\begin{tikzpicture}\n");
                writer.write("\\node (dateplace) {\\begin{minipage}{0.4\\textwidth}\\begin{center}\\reviewplace, den \\reviewdate{}\\end{center}\\end{minipage}};\n");
                writer.write("\\node (title) [below=2 of dateplace] {\\begin{minipage}{5cm}\\begin{center}\\prof{Thomas Ströder}\\end{center}\\end{minipage}};\n");
                writer.write("\\node (signature) [above=0.1 of title,xshift=5mm] {\\includegraphics[height=1.5cm]{\\signaturepath}};\n");
                writer.write("\\node (title2) [right=4 of title.north east,anchor=north west] {\\begin{minipage}{5cm}\\begin{center}\\otherreviewer{}\\end{center}\\end{minipage}};\n");
                writer.write("\\node (statement) [above=3 of title2] {\\begin{minipage}{0.4\\textwidth}\\begin{center}Als Zweitgutachter stimme ich dem Gutachten des Erstgutachters zu.\\end{center}\\end{minipage}};\n");
                writer.write("\\draw ($(title.north west)+(0,0.1)$) -- ($(title.north east)+(0,0.1)$);\n");
                writer.write("\\draw ($(title2.north west)+(0,0.1)$) -- ($(title2.north east)+(0,0.1)$);\n");
                writer.write("\\end{tikzpicture}\n");
                writer.write("\\end{center}\n\n");
            } else {
                writer.write("\\begin{flushright}\n");
                writer.write(review.place());
                writer.write(", den ");
                writer.write(review.date());
                writer.write("\n\n");
                writer.write("\\vspace*{4ex}\n\n");
                writer.write("\\includegraphics[height=1.5cm]{");
                writer.write(review.signature());
                writer.write("}\\hspace*{-10mm}\n\n");
                writer.write("\\begin{minipage}{5cm}\n");
                writer.write("\\hrulefill\n\n");
                writer.write("\\vspace*{-2ex}\n\n");
                writer.write("\\begin{center}\n");
                writer.write(review.reviewer());
                writer.write("\n");
                writer.write("\\end{center}\n");
                writer.write("\\end{minipage}\n\n");
                writer.write("\\end{flushright}\n\n");
            }
            writer.write("\\end{document}\n");
        }
        return result;
    }

    private static Criteria parseCriteria(final File reviewFile, final Review review) throws IOException {
        final File criteriaFile =
            reviewFile.toPath().toAbsolutePath().getParent().resolve(review.criteriaPath()).toFile();
        try (BufferedReader reader = new BufferedReader(new FileReader(criteriaFile))) {
            return Main.GSON.fromJson(reader, CriteriaRaw.class).toCriteria();
        }
    }

    private static String toGrade(final BigFraction percent) {
        if (percent.compareTo(new BigFraction(97,100)) >= 0) {
            return "1{,}0";
        }
        if (percent.compareTo(new BigFraction(92,100)) >= 0) {
            return "1{,}3";
        }
        if (percent.compareTo(new BigFraction(89,100)) >= 0) {
            return "1{,}7";
        }
        if (percent.compareTo(new BigFraction(85,100)) >= 0) {
            return "2{,}0";
        }
        if (percent.compareTo(new BigFraction(81,100)) >= 0) {
            return "2{,}3";
        }
        if (percent.compareTo(new BigFraction(77,100)) >= 0) {
            return "2{,}7";
        }
        if (percent.compareTo(new BigFraction(72,100)) >= 0) {
            return "3{,}0";
        }
        if (percent.compareTo(new BigFraction(67,100)) >= 0) {
            return "3{,}3";
        }
        if (percent.compareTo(new BigFraction(59,100)) >= 0) {
            return "3{,}7";
        }
        if (percent.compareTo(new BigFraction(50,100)) >= 0) {
            return "4{,}0";
        }
        return "5{,}0";
    }

    private static File toOutputFile(final File reviewFile) {
        final String name = reviewFile.getName();
        return
            reviewFile
            .toPath()
            .toAbsolutePath()
            .getParent()
            .resolve(name.substring(0, name.length() - 4) + "tex")
            .toFile();
    }

}
