package theseshelper.review;
import java.util.*;

import org.apache.commons.math3.fraction.*;

import theseshelper.*;

public abstract class ReviewTemplate {

    private static final List<ReviewEvaluation> FORMAL =
        List.of(
            new ReviewEvaluation("appearance", BigFraction.ONE),
            new ReviewEvaluation("distances", BigFraction.ONE),
            new ReviewEvaluation("lists", BigFraction.ONE),
            new ReviewEvaluation("spelling", BigFraction.TWO, EvaluationMode.SPELLING),
            new ReviewEvaluation("grammar", BigFraction.TWO),
            new ReviewEvaluation("punctuation", BigFraction.ONE),
            new ReviewEvaluation("language", BigFraction.TWO),
            new ReviewEvaluation("figuresquality", BigFraction.TWO),
            new ReviewEvaluation("quotingstyle", BigFraction.ONE),
            new ReviewEvaluation("quotinglookup", BigFraction.ONE),
            new ReviewEvaluation("literaturestyle", BigFraction.ONE),
            new ReviewEvaluation("literatureprops", BigFraction.TWO),
            new ReviewEvaluation("diligence", BigFraction.ONE)
        );

    private static final List<ReviewEvaluation> STRUCTURE =
        List.of(
            new ReviewEvaluation("toccontributions", BigFraction.ONE),
            new ReviewEvaluation("tocmethods", BigFraction.ONE),
            new ReviewEvaluation("titlematch", BigFraction.ONE),
            new ReviewEvaluation("goal", BigFraction.TWO),
            new ReviewEvaluation("contributions", BigFraction.TWO),
            new ReviewEvaluation("methodoverview", BigFraction.ONE),
            new ReviewEvaluation("structureoverview", BigFraction.ONE),
            new ReviewEvaluation("structurequality", BigFraction.ONE),
            new ReviewEvaluation("structurebridge", BigFraction.ONE),
            new ReviewEvaluation("structurefast", BigFraction.ONE),
            new ReviewEvaluation("references", BigFraction.ONE),
            new ReviewEvaluation("basicscontributionsfit", BigFraction.ONE),
            new ReviewEvaluation("illustration", new BigFraction(4)),
            new ReviewEvaluation("future", BigFraction.ONE),
            new ReviewEvaluation("conclusioncontribution", BigFraction.ONE),
            new ReviewEvaluation("conclusiongoal", BigFraction.ONE),
            new ReviewEvaluation("conclusionvalue", BigFraction.ONE)
        );

    private static final List<ReviewEvaluation> VALUE =
        List.of(
            new ReviewEvaluation("effort", BigFraction.TWO),
            new ReviewEvaluation("relevance", BigFraction.TWO),
            new ReviewEvaluation("innovativeness", BigFraction.ONE),
            new ReviewEvaluation("level", BigFraction.ONE),
            new ReviewEvaluation("applicability", BigFraction.TWO),
            new ReviewEvaluation("value", BigFraction.TWO)
        );

    public static Review selectReviewTemplate(final ThesisType thesisType, final Result resultFile) {
        return new Review(
            true,
            ReviewTemplate.toVersion(thesisType),
            thesisType,
            resultFile.title(),
            String.format("%s %s", resultFile.givennames(), resultFile.familynames()),
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
            new BigFraction(100),
            null
        );
    }

    private static List<ReviewEvaluationGroup> selectEvaluationGroups(final ThesisType thesisType) {
        if (thesisType == ThesisType.MA) {
            return List.of(
                new ReviewEvaluationGroup(
                    "Aufbau der Arbeit, Strukturierung der Bearbeitung",
                    new BigFraction(15, 100),
                    ReviewTemplate.STRUCTURE,
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Wissenschaftliches Vorgehen",
                    new BigFraction(40, 100),
                    List.of(
                        new ReviewEvaluation("literatureamount", new BigFraction(4)),
                        new ReviewEvaluation("literaturequality", new BigFraction(4)),
                        new ReviewEvaluation("quotingapplication", new BigFraction(4)),
                        new ReviewEvaluation("relatedlookup", BigFraction.ONE),
                        new ReviewEvaluation("relatedamount", new BigFraction(4)),
                        new ReviewEvaluation("relatedcontent", BigFraction.ONE),
                        new ReviewEvaluation("relateddifference", BigFraction.ONE),
                        new ReviewEvaluation("relatednew", BigFraction.ONE),
                        new ReviewEvaluation("basicscorrect", BigFraction.ONE),
                        new ReviewEvaluation("methodintro", BigFraction.ONE),
                        new ReviewEvaluation("methodreason", BigFraction.ONE),
                        new ReviewEvaluation("methodcover", new BigFraction(3)),
                        new ReviewEvaluation("methodapplication", new BigFraction(4)),
                        new ReviewEvaluation("objectivity", BigFraction.TWO),
                        new ReviewEvaluation("reliability", BigFraction.TWO),
                        new ReviewEvaluation("validity", new BigFraction(4)),
                        new ReviewEvaluation("limitations", BigFraction.TWO),
                        new ReviewEvaluation("comprehensibility", new BigFraction(4))
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Praktische Relevanz der Arbeit, Umsetzbarkeit der erarbeiteten Ergebnisse",
                    new BigFraction(30, 100),
                    ReviewTemplate.VALUE,
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Formale Ordnungsmäßigkeit der Arbeit",
                    new BigFraction(15, 100),
                    ReviewTemplate.FORMAL,
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
                    ReviewTemplate.STRUCTURE,
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Wissenschaftliches Vorgehen",
                    new BigFraction(30, 100),
                    List.of(
                        new ReviewEvaluation("literatureamount", new BigFraction(3)),
                        new ReviewEvaluation("literaturequality", new BigFraction(3)),
                        new ReviewEvaluation("quotingapplication", new BigFraction(3)),
                        new ReviewEvaluation("relatedlookup", BigFraction.ONE),
                        new ReviewEvaluation("relatedamount", new BigFraction(3)),
                        new ReviewEvaluation("relatedcontent", BigFraction.ONE),
                        new ReviewEvaluation("relateddifference", BigFraction.ONE),
                        new ReviewEvaluation("relatednew", BigFraction.ONE),
                        new ReviewEvaluation("basicscorrect", BigFraction.ONE),
                        new ReviewEvaluation("methodintro", BigFraction.ONE),
                        new ReviewEvaluation("methodreason", BigFraction.ONE),
                        new ReviewEvaluation("methodcover", BigFraction.TWO),
                        new ReviewEvaluation("methodapplication", new BigFraction(3)),
                        new ReviewEvaluation("objectivity", BigFraction.TWO),
                        new ReviewEvaluation("reliability", BigFraction.TWO),
                        new ReviewEvaluation("validity", BigFraction.TWO),
                        new ReviewEvaluation("limitations", BigFraction.TWO),
                        new ReviewEvaluation("comprehensibility", new BigFraction(3))
                    ),
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Praktische Relevanz der Arbeit, Umsetzbarkeit der erarbeiteten Ergebnisse",
                    new BigFraction(30, 100),
                    ReviewTemplate.VALUE,
                    null,
                    null,
                    "\\pagebreak",
                    true
                ),
                new ReviewEvaluationGroup(
                    "Formale Ordnungsmäßigkeit der Arbeit",
                    new BigFraction(20, 100),
                    ReviewTemplate.FORMAL,
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
            return "BA 4.1";
        case MA:
            return "MA 4.1";
        default:
            throw new IllegalArgumentException("No version known for argument " + thesisType.title + "!");
        }
    }

}
