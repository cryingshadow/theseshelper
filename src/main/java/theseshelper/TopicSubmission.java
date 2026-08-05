package theseshelper;

import java.util.*;

public record TopicSubmission(
    String type,
    String studentGivenNames,
    String studentFamilyNames,
    Date due,
    boolean submitted,
    boolean graded,
    Optional<String> otherExaminer,
    boolean firstExaminer,
    Optional<Date> colloquium,
    String location
) implements Comparable<TopicSubmission> {

    private static final Comparator<TopicSubmission> COMPARATOR =
        new LexicographicComparator<TopicSubmission>(
            LexicographicComparator.toComparator(TopicSubmission::due),
            LexicographicComparator.toComparator(TopicSubmission::type),
            LexicographicComparator.toComparator(TopicSubmission::studentFamilyNames),
            LexicographicComparator.toComparator(TopicSubmission::studentGivenNames),
            LexicographicComparator.toComparator((final TopicSubmission s) -> s.otherExaminer().orElse(""))
        );

    public static final Comparator<TopicSubmission> COLLOQUIA_COMPARATOR =
        new LexicographicComparator<TopicSubmission>(
            LexicographicComparator.toComparator((final TopicSubmission s) -> s.colloquium().orElse(null)),
            LexicographicComparator.toComparator((final TopicSubmission s) -> s.otherExaminer().orElse("")),
            LexicographicComparator.toComparator(TopicSubmission::studentFamilyNames),
            LexicographicComparator.toComparator(TopicSubmission::studentGivenNames)
        );

    @Override
    public int compareTo(final TopicSubmission o) {
        return TopicSubmission.COMPARATOR.compare(this, o);
    }

}
