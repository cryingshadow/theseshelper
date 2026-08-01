package theseshelper.templates;

import java.io.*;
import java.util.*;

import org.apache.commons.math3.fraction.*;

import theseshelper.*;
import theseshelper.review.*;

public abstract class ReviewTemplate {

    public static Review selectReviewTemplate(final ThesisType thesisType, final Result resultFile) {
        return new Review(
            true,
            ReviewTemplate.toVersion(thesisType),
            thesisType,
            resultFile.title(),
            resultFile.name(),
            "\\today",
            "Essen",
            "\\prof{Thomas Ströder}",
            "../../../../../Bilder/signature.png",
            false,
            false,
            resultFile.optionalOtherReviewer().get(),
            List.of("Beitrag"),
            ReviewTemplate.selectEvaluationGroups(thesisType),
            "../../../../../templates/review/criteria.json",
            false,
            null,
            null,
            null,
            null,
            null,
            new BigFraction(100)
        );
    }

    private static List<ReviewEvaluationGroup> selectEvaluationGroups(final ThesisType thesisType) {
        if (thesisType == ThesisType.MA) {
            return List.of(
                new ReviewEvaluationGroup(
                    "Aufbau der Arbeit, Strukturierung der Bearbeitung",
                    new BigFraction(15, 100),
                    List.of(
                        new ReviewEvaluation("appearance", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("tocquality", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("lists", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("goal", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("contributions", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("methodoverview", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structureoverview", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structurequality", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("references", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conceptsintroduced", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("basicsused", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("future", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conclusion", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    null
                ),
                new ReviewEvaluationGroup(
                    "Wissenschaftliches Vorgehen",
                    new BigFraction(40, 100),
                    List.of(
                        new ReviewEvaluation("literatureamount", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("literaturequality", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("quotingapplication", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("relatedlookup", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relatedamount", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("relatedcontent", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relateddifference", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relatednew", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodintro", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodreason", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodcover", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("methodapplication", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("objectivity", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("reliability", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("validity", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("comprehensibility", null, new BigFraction(4), null, null, null)
                    ),
                    null,
                    null,
                    null
                ),
                new ReviewEvaluationGroup(
                    "Praktische Relevanz der Arbeit, Umsetzbarkeit der erarbeiteten Ergebnisse",
                    new BigFraction(30, 100),
                    List.of(
                        new ReviewEvaluation("innovativeness", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relevance", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("level", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("applicability", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("value", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    null
                ),
                new ReviewEvaluationGroup(
                    "Formale Ordnungsmäßigkeit der Arbeit",
                    new BigFraction(15, 100),
                    List.of(
                        new ReviewEvaluation("distances", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("spelling", null, new BigFraction(3), null, null, EvaluationMode.SPELLING),
                        new ReviewEvaluation("grammar", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("punctuation", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("language", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("illustration", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("figuresquality", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("quotingstyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("quotinglookup", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literaturestyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literatureprops", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("diligence", null, new BigFraction(3), null, null, EvaluationMode.EXTRA)
                    ),
                    null,
                    null,
                    null
                )
            );
        } else {
            return List.of(
                new ReviewEvaluationGroup(
                    "Aufbau der Arbeit, Strukturierung der Bearbeitung",
                    new BigFraction(20, 100),
                    List.of(
                        new ReviewEvaluation("appearance", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("toccontributions", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("tocmethods", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("titlematch", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("lists", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("goal", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("contributions", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("methodoverview", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structureoverview", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structurequality", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structurebridge", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structurefast", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("references", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conceptsintroduced", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("basicsused", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("future", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conclusioncontribution", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conclusiongoal", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    null
                ),
                new ReviewEvaluationGroup(
                    "Wissenschaftliches Vorgehen",
                    new BigFraction(30, 100),
                    List.of(
                        new ReviewEvaluation("literatureamount", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("literaturequality", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("quotingapplication", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("relatedlookup", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relatedamount", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("relatedcontent", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relateddifference", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relatednew", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodintro", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodreason", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodcover", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("methodapplication", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("objectivity", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("reliability", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("validity", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("comprehensibility", null, new BigFraction(3), null, null, null)
                    ),
                    null,
                    null,
                    null
                ),
                new ReviewEvaluationGroup(
                    "Praktische Relevanz der Arbeit, Umsetzbarkeit der erarbeiteten Ergebnisse",
                    new BigFraction(30, 100),
                    List.of(
                        new ReviewEvaluation("innovativeness", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relevance", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("level", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("applicability", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("value", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    null
                ),
                new ReviewEvaluationGroup(
                    "Formale Ordnungsmäßigkeit der Arbeit",
                    new BigFraction(20, 100),
                    List.of(
                        new ReviewEvaluation("distances", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("spelling", null, new BigFraction(3), null, null, EvaluationMode.SPELLING),
                        new ReviewEvaluation("grammar", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("punctuation", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("language", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("illustration", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("figuresquality", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("quotingstyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("quotinglookup", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literaturestyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literatureprops", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("diligence", null, new BigFraction(3), null, null, EvaluationMode.EXTRA)
                    ),
                    null,
                    null,
                    null
                )
            );
        }
    }

    private static String toVersion(final ThesisType thesisType) {
        switch (thesisType) {
        case BA:
        case PA:
            return "BA 3.1";
        case MA:
            return "MA 3.2";
        default:
            throw new IllegalArgumentException("No version known for argument " + thesisType.title + "!");
        }
    }

    public boolean isOlderVersion(final String version) {
        final String type = version.substring(0, 2);
        final String currentVersion = this.version();
        final String currentType = currentVersion.substring(0, 2);
        if (!type.equals(currentType)) {
            return false;
        }
        final String[] versionNumber = version.substring(3).split("\\.");
        final String[] currentVersionNumber = currentVersion.substring(3).split("\\.");
        int i = 0;
        while (i < versionNumber.length && i < currentVersionNumber.length) {
            final int compare =
                Integer.compare(Integer.parseInt(versionNumber[i]), Integer.parseInt(currentVersionNumber[i]));
            if (compare < 0) {
                return true;
            } else if (compare > 0) {
                return false;
            }
            i++;
        }
        if (versionNumber.length < currentVersionNumber.length) {
            while (i < currentVersionNumber.length) {
                if (Integer.parseInt(currentVersionNumber[i]) > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void writeTemplate(
        final String author,
        final String title,
        final Optional<String> otherReviewer,
        final BufferedWriter writer
    ) throws IOException {
        writer.write("%version: ");
        writer.write(this.version());
        writer.write("\n%empty\n");
        writer.write("\\documentclass{article}\n\n");
        writer.write("\\input{../../../../../templates/review/packages.tex}\n\n");
        writer.write("\\newcommand{\\thesistype}{");
        writer.write(this.thesisType());
        writer.write("}\n");
        writer.write("\\newcommand{\\thesistitle}{");
        writer.write(title);
        writer.write("}\n");
        writer.write("\\newcommand{\\thesisauthor}{");
        writer.write(author);
        writer.write("}\n");
        writer.write("\\newcommand{\\reviewdate}{\\today}\n");
        writer.write("\\newcommand{\\reviewplace}{Essen}\n");
        writer.write("\\newcommand{\\signaturepath}{../../../../../Bilder/signature.png}\n");
        writer.write("\\setboolean{restrictionnote}{false}\n");
        writer.write("\\setboolean{pagebreakmiddle}{true}\n");
        writer.write("\\setboolean{pagebreaktotal}{false}\n");
        writer.write("\\setboolean{tworeviewers}{false}\n");
        writer.write("\\newcommand{\\otherreviewer}{");
        if (otherReviewer.isPresent()) {
            writer.write(otherReviewer.get());
        }
        writer.write("}\n");
        writer.write("\n\\newcommand{\\structureReview}{%\n");
        this.writeStructureTemplate(writer);
        writer.write("}\n\n");
        writer.write("\\newcommand{\\methodsReview}{%\n");
        this.writeMethodsTemplate(writer);
        writer.write("}\n\n");
        writer.write("\\newcommand{\\contentReview}{%\n");
        this.writeContentTemplate(writer);
        writer.write("}\n\n");
        writer.write("\\newcommand{\\formalReview}{%\n");
        this.writeFormalTemplate(writer);
        writer.write("}\n\n");
        writer.write("\\newcommand{\\totalReview}{%\n");
        writer.write(
            "Insgesamt wurden \\evaluationpoints{} Punkte erreicht und das Gesamturteil lautet: \\grade\n"
        );
        writer.write("}\n\n");
        writer.write("\\input{../../../../../templates/review/review.tex}\n");
    }

    protected abstract String version();

    abstract String thesisType();

    void writeContentTemplate(final BufferedWriter writer) throws IOException {
        writer.write("Die Arbeit liefert die folgenden inhaltlichen Beiträge:\n");
        writer.write("\\begin{itemize}\n");
        writer.write("\\item Beitrag 1\n");
        writer.write("\\end{itemize}\n");
        writer.write("\\innovativenessvi{}\n");
        writer.write("\\relevancevi{}\n");
        writer.write("\\levelvi{}\n");
        writer.write("\\applicabilityvi{}\n");
        writer.write("\\valuevi{}\n");
        writer.write("\\evaluationpartresult{30}\n");
    }

    abstract void writeFormalTemplate(BufferedWriter writer) throws IOException;

    abstract void writeMethodsTemplate(BufferedWriter writer) throws IOException;

    abstract void writeStructureTemplate(BufferedWriter writer) throws IOException;

}
