package theseshelper;

import java.util.*;
import java.util.stream.*;

public enum Mode {

    CRITERIA("Pretty print criteria.", Set.of(Flag.INPUT)),

    POINTS("Compute points for year.", Set.of(Flag.DIRECTORY, Flag.YEAR)),

    PREPARATION("Prepare theses for year.", Set.of(Flag.DIRECTORY, Flag.YEAR)),

    REVIEW("Create review.", Set.of(Flag.INPUT)),

    SPELLCHECK("Spell check theses for year.", Set.of(Flag.DIRECTORY, Flag.YEAR)),

    STATISTICS(
        "Compile statistics for year(s).",
        Set.of(Flag.DIRECTORY, Flag.REVIEWER_TYPE, Flag.THESIS_TYPE, Flag.YEAR)
    ),

    UNFINISHED("Show unfinished theses.", Set.of(Flag.DIRECTORY));

    public final String description;

    public final Set<Flag> parameters;

    private Mode(final String description, final Set<Flag> parameters) {
        this.description = description;
        this.parameters = parameters;
    }

    public String help() {
        return String.format(
            "%s (%s)",
            this.description,
            this.parameters.stream().map(Flag::name).collect(Collectors.joining(", "))
        );
    }

}
