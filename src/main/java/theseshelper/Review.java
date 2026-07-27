package theseshelper;

import java.util.*;

public record Review(
    ThesisType type,
    String title,
    String student,
    String date,
    String place,
    String signature,
    Boolean restricted,
    Boolean pagebreakmiddle,
    Boolean pagebreaktotal,
    Boolean tworeviewers,
    String otherReviewer,
    List<String> contributions,
    String structureStart,
    List<ReviewEvaluation> structureCriteria,
    String methodsStart,
    List<ReviewEvaluation> methodsCriteria,
    String contentsStart,
    List<ReviewEvaluation> contentsCriteria,
    String formalStart,
    List<ReviewEvaluation> formalCriteria,
    String bonusStart,
    ReviewEvaluation bonus,
    String totalStart,
    String alternativeTotalText,
    String additionalTotalText
) {

}
