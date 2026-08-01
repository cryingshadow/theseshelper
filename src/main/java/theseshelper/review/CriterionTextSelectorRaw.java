package theseshelper.review;

import java.util.*;

public record CriterionTextSelectorRaw(
    String name,
    String prefix,
    List<CriterionTextRaw> texts,
    String defaulttext,
    String suffix
) {

}
