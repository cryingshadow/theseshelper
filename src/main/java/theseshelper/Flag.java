package theseshelper;

import clit.*;

public enum Flag implements Parameter {

    DIRECTORY("d", "directory", "Root directory."),

    INPUT("i", "input", "File for input."),

    MODE("m", "mode", "Execution mode (STATISTICS, CLASS, LIST, QUIZ, REVIEWER, CRITERIA, TALK)."),

    OUTPUT("o", "output", "File for output."),

    REVIEWER_TYPE("r", "reviewer", "Reviewer type (ALL, FIRST, SECOND)."),

    THESIS_TYPE("t", "thesis", "Thesis type (ALL, ALL_BUT_PA, BA, MA, PA)."),

    VERBOSITY("v", "verbosity", "Verbosity of logging."),

    YEAR("y", "year", "Year(s) to consider.");

    private final String description;

    private final String longName;

    private final String shortName;

    private Flag(final String shortName, final String longName, final String description) {
        this.shortName = shortName;
        this.longName = longName;
        this.description = description;
    }

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public String longName() {
        return this.longName;
    }

    @Override
    public String shortName() {
        return this.shortName;
    }

}
