package theseshelper.review;
import java.util.*;

import org.apache.commons.math3.fraction.*;

import theseshelper.*;

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
            "Die Arbeit verfolgt",
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
                        new ReviewEvaluation("tocquality", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("lists", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("goal", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("contributions", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("methodoverview", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structureoverview", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("structurequality", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("references", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("basicscontributionsfit", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("illustration", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("future", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conclusion", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
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
                        new ReviewEvaluation("basicscorrect", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodintro", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodreason", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodcover", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("methodapplication", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("objectivity", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("reliability", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("validity", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("limitations", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("comprehensibility", null, new BigFraction(4), null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Praktische Relevanz der Arbeit, Umsetzbarkeit der erarbeiteten Ergebnisse",
                    new BigFraction(30, 100),
                    List.of(
                        new ReviewEvaluation("effort", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("innovativeness", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relevance", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("level", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("applicability", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("value", null, BigFraction.TWO, null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Formale Ordnungsmäßigkeit der Arbeit",
                    new BigFraction(15, 100),
                    List.of(
                        new ReviewEvaluation("appearance", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("distances", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("spelling", null, BigFraction.TWO, null, null, EvaluationMode.SPELLING),
                        new ReviewEvaluation("grammar", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("punctuation", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("language", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("figuresquality", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("quotingstyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("quotinglookup", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literaturestyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literatureprops", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("diligence", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                )
            );
        } else {
            return List.of(
                new ReviewEvaluationGroup(
                    "Aufbau der Arbeit, Strukturierung der Bearbeitung",
                    new BigFraction(20, 100),
                    List.of(
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
                        new ReviewEvaluation("basicscontributionsfit", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("illustration", null, new BigFraction(4), null, null, null),
                        new ReviewEvaluation("future", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conclusioncontribution", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("conclusiongoal", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
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
                        new ReviewEvaluation("basicscorrect", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodintro", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodreason", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("methodcover", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("methodapplication", null, new BigFraction(3), null, null, null),
                        new ReviewEvaluation("objectivity", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("reliability", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("validity", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("limitations", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("comprehensibility", null, new BigFraction(3), null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Praktische Relevanz der Arbeit, Umsetzbarkeit der erarbeiteten Ergebnisse",
                    new BigFraction(30, 100),
                    List.of(
                        new ReviewEvaluation("effort", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("innovativeness", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("relevance", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("level", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("applicability", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("value", null, BigFraction.TWO, null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Formale Ordnungsmäßigkeit der Arbeit",
                    new BigFraction(20, 100),
                    List.of(
                        new ReviewEvaluation("appearance", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("distances", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("spelling", null, BigFraction.TWO, null, null, EvaluationMode.SPELLING),
                        new ReviewEvaluation("grammar", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("punctuation", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("language", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("figuresquality", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("quotingstyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("quotinglookup", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literaturestyle", null, BigFraction.ONE, null, null, null),
                        new ReviewEvaluation("literatureprops", null, BigFraction.TWO, null, null, null),
                        new ReviewEvaluation("diligence", null, BigFraction.ONE, null, null, null)
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                )
            );
        }
    }

    private static String toVersion(final ThesisType thesisType) {
        switch (thesisType) {
        case BA:
        case PA:
            return "BA 4.0";
        case MA:
            return "MA 4.0";
        default:
            throw new IllegalArgumentException("No version known for argument " + thesisType.title + "!");
        }
    }

}
