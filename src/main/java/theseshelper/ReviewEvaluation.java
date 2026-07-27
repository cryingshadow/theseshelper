package theseshelper;

public record ReviewEvaluation(
    String criterion,
    Integer actual,
    Integer expected,
    String alternativeText,
    String additionalText
) {

}
