package theseshelper.review;

import java.io.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;

import org.apache.commons.math3.fraction.*;

import theseshelper.*;

public abstract class ReviewWriter {

    private static final String SPELLING_ERROR_TEXT = " durch eine automatische Rechtschreibprüfung gefunden ";

    public static File write(final File reviewFile) throws IOException {
        final Review review = Review.parse(reviewFile);
        final Criteria criteria = ReviewWriter.parseCriteria(reviewFile, review);
        final File result = ReviewWriter.toOutputFile(reviewFile);
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(result))) {
            ReviewWriter.writeHeader(review, writer);
            ReviewWriter.writeContent(review, writer);
            ReviewWriter.writeTotal(review, writer);
            ReviewWriter.writeSignature(review, writer);
            writer.write("\\pagebreak\n\n");
            final BigFraction weightSum = ReviewEvaluator.weightSum(review);
            for (final ReviewEvaluationGroup group : review.evaluationGroups()) {
                ReviewWriter.writeGroup(group, criteria, weightSum, review, writer);
            }
            writer.write("\\end{document}\n");
        }
        return result;
    }

    private static Criteria parseCriteria(final File reviewFile, final Review review) throws IOException {
        return Criteria.parseCriteria(
            reviewFile.toPath().toAbsolutePath().getParent().resolve(review.criteriaPath()).toFile()
        );
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

    private static void writeContent(final Review review, final BufferedWriter writer) throws IOException {
        writer.write("\\section{Inhalt}\n");
        writer.write(review.goal());
        writer.write("\\\\\n");
        if (review.contributions().isEmpty()) {
            writer.write("Leider erbringt die Arbeit keine wissenschaftlichen Eigenbeiträge.\n\n");
        } else {
            writer.write("Dazu werden die folgenden Beiträge erbracht:\n");
            writer.write("\\begin{itemize}\n");
            for (final String contribution : review.contributions()) {
                writer.write("\\item ");
                writer.write(contribution);
                writer.write("\n");
            }
            writer.write("\\end{itemize}\n\n");
        }
    }

    private static void writeEvaluation(
        final ReviewEvaluation evaluation,
        final Criteria criteria,
        final BufferedWriter writer
    ) throws IOException {
        if (evaluation.evaluation() == null) {
            return;
        }
        if (evaluation.alternative() != null && !evaluation.alternative().isBlank()) {
            writer.write(evaluation.alternative());
            return;
        }
        switch (evaluation.evaluationMode()) {
        case SPELLING:
            writer.write("Die Arbeit enthält ");
            final String errors = evaluation.evaluation().toString();
            switch (errors) {
            case "0":
                writer.write("keine Rechtschreibfehler, welche");
                writer.write(ReviewWriter.SPELLING_ERROR_TEXT);
                writer.write("wurden.");
                break;
            case "1":
                writer.write("einen Rechtschreibfehler, welcher");
                writer.write(ReviewWriter.SPELLING_ERROR_TEXT);
                writer.write("wurde.");
                break;
            default:
                writer.write(errors);
                writer.write(" Rechtschreibfehler, welche");
                writer.write(ReviewWriter.SPELLING_ERROR_TEXT);
                writer.write("wurden.");
            }
            break;
        case BONUS:
            writer.write("Es wurde");
            final String bonusPoints = evaluation.evaluation().toString();
            if ("1".equals(bonusPoints)) {
                writer.write(" 1 Bonuspunkt");
            } else {
                writer.write("n ");
                writer.write(bonusPoints);
                writer.write(" Bonuspunkte");
            }
            writer.write(" gewährt.\n");
            break;
        default:
            final CriterionTextSelector selector = criteria.get(evaluation.criterion());
            if (selector == null) {
                Main.LOGGER.log(
                    Level.WARNING,
                    String.format("Criterion %s not found!", evaluation.criterion())
                );
                writer.write(evaluation.criterion());
            } else {
                writer.write(selector.apply(evaluation.evaluation()));
            }
            writer.write("\n");
        }
        if (evaluation.additional() != null && !evaluation.additional().isBlank()) {
            writer.write(evaluation.additional());
            writer.write("\n");
        }
    }

    private static void writeGroup(
        final ReviewEvaluationGroup group,
        final Criteria criteria,
        final BigFraction weightSum,
        final Review review,
        final BufferedWriter writer
    ) throws IOException {
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
            ReviewWriter.writeEvaluation(evaluation, criteria, writer);
        }
        if (group.diagram() != null && group.diagram()) {
            ReviewWriter.writeKiviatDiagram(group, criteria, writer);
        }
        writer.write("\\begin{flushright}{Bewertung: ");
        final BigFraction result = ReviewEvaluator.evaluate(group, review.totalExpected(), weightSum);
        Main.LOGGER.log(
            Level.FINE,
            String.format(
                "Evaluation %s: %d + %s",
                group.title(),
                result.intValue(),
                result.subtract(result.intValue()).toString()
            )
        );
        writer.write(String.valueOf(result.intValue()));
        writer.write(" von ");
        writer.write(ReviewEvaluator.total(group, review.totalExpected(), weightSum).toString());
        writer.write(" Punkten}\\end{flushright}\n\n");
    }

    private static void writeHeader(final Review review, final BufferedWriter writer) throws IOException {
        writer.write("\\documentclass{article}\n\n");
        writer.write("\\usepackage{fhdwutil}\n");
        writer.write("\\usepackage{fhdwevaluation}\n");
        writer.write("\\usepackage[a4paper,margin=2.5cm]{geometry}\n");
        writer.write("\\usepackage{setspace}\n");
        writer.write("\\usepackage{graphicx}\n");
        writer.write("\\usepackage{xcolor}\n");
        writer.write("\\usepackage{adjustbox}\n");
        writer.write("\\usepackage{tikz}\n");
        writer.write("\\usetikzlibrary{arrows,shapes,chains,matrix,positioning,scopes,decorations.pathmorphing,");
        writer.write("decorations.pathreplacing,shadows,calc,trees}\n");
        writer.write("\\usepackage{tkz-kiviat}\n\n");
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
    }

    private static void writeKiviatDiagram(
        final ReviewEvaluationGroup group,
        final Criteria criteria,
        final BufferedWriter writer
    ) throws IOException {
        final List<String> criteriaForDiagram =
            group
            .evaluations()
            .stream()
            .filter(ev -> !ev.unused())
            .map(ev -> {
                final CriterionTextSelector criterion = criteria.get(ev.criterion());
                return criterion == null ? null : criterion.name;
            }).filter(s -> s != null)
            .toList();
        if (!criteriaForDiagram.isEmpty()) {
            writer.write("\n\\vspace*{1ex}\n\n");
            writer.write("\\begin{adjustbox}{max width=\\linewidth,center}\n");
            writer.write("\\begin{tikzpicture}\n");
            writer.write("\\tkzKiviatDiagram{");
            writer.write(criteriaForDiagram.stream().collect(Collectors.joining(",")));
            writer.write("}\n");
            writer.write("\\tkzKiviatLine[thick,color=blue,mark=none,fill=blue!20,opacity=.5](");
            writer.write(
                group
                .evaluations()
                .stream()
                .filter(ev -> !ev.unused())
                .map(ev -> criteria.get(ev.criterion()) == null ? null : ev.evaluationForDiagram())
                .filter(s -> s != null)
                .collect(Collectors.joining(","))
            );
            writer.write(")\n");
            writer.write("\\end{tikzpicture}\n");
            writer.write("\\end{adjustbox}\n\n");
        }
    }

    private static void writeSignature(final Review review, final BufferedWriter writer) throws IOException {
        writer.write("\\vspace*{5ex}\n\n");
        if (review.twoReviewers()) {
            writer.write("\\begin{center}\n");
            writer.write("\\begin{tikzpicture}\n");
            writer.write("\\node (dateplace) {\\begin{minipage}{0.4\\textwidth}\\begin{center}");
            writer.write(review.place());
            writer.write(", den ");
            writer.write(review.date());
            writer.write("\\end{center}\\end{minipage}};\n");
            writer.write("\\node (title) [below=2 of dateplace] {\\begin{minipage}{5cm}\\begin{center}");
            writer.write(review.reviewer());
            writer.write("\\end{center}\\end{minipage}};\n");
            writer.write("\\node (signature) [above=0.1 of title,xshift=5mm] {");
            writer.write("\\includegraphics[height=1.5cm]{");
            writer.write(review.signature());
            writer.write("}};\n");
            writer.write("\\node (title2) [right=4 of title.north east,anchor=north west] {");
            writer.write("\\begin{minipage}{5cm}\\begin{center}");
            writer.write(review.otherReviewer());
            writer.write("\\end{center}\\end{minipage}};\n");
            writer.write("\\node (statement) [above=3 of title2] {\\begin{minipage}{0.4\\textwidth}\\begin{center}");
            writer.write("Als Zweitgutachter stimme ich dem Gutachten des Erstgutachters zu.");
            writer.write("\\end{center}\\end{minipage}};\n");
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
        writer.write("\\vfill\n\n");
        writer.write("\\noindent Auf den nachfolgenden Seiten wird diese Bewertung näher erläutert.\n\n");
    }

    private static void writeTotal(final Review review, final BufferedWriter writer) throws IOException {
        if (review.pagebreakTotal() != null && review.pagebreakTotal()) {
            writer.write("\\pagebreak\n\n");
        }
        writer.write("\\section{Gesamtbeurteilung}\n");
        if (review.bonusStart() != null && !review.bonusStart().isBlank()) {
            writer.write(review.bonusStart());
            writer.write("\n");
        }
        if (review.bonus() != null) {
            writer.write("Es wurde");
            final String bonusPoints = review.bonus().toString();
            if ("1".equals(bonusPoints)) {
                writer.write(" 1 Bonuspunkt");
            } else {
                writer.write("n ");
                writer.write(bonusPoints);
                writer.write(" Bonuspunkte");
            }
            writer.write(" gewährt.\n");
        }
        if (review.totalStart() != null && !review.totalStart().isBlank()) {
            writer.write(review.totalStart());
            writer.write("\n");
        }
        final int achieved = ReviewEvaluator.evaluate(review);
        if (review.alternativeTotalText() != null && !review.alternativeTotalText().isBlank()) {
            writer.write(review.alternativeTotalText());
            writer.write("\n");
        } else {
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
            writer.write(ReviewEvaluator.toGradeForLaTeX(review));
            writer.write("}}\\end{center}\n\n");
        }
        if (review.additionalTotalText() != null && !review.additionalTotalText().isBlank()) {
            writer.write(review.additionalTotalText());
            writer.write("\n");
        }
        if (review.hasUnusedCriterion()) {
            writer.write("\\footnote{\\textcolor{red}{Nicht alle Kriterien wurden bewertet!}}\n\n");
        }
        if (ReviewEvaluator.isCornerValue(review) && (review.corner() == null || !review.corner())) {
            writer.write("\\footnote{\\textcolor{red}{Ergebnis ist grenzwertig!}}\n\n");
        }
    }

}
