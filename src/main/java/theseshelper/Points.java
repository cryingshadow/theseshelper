package theseshelper;

import java.time.*;
import java.util.*;

public class Points {

    public List<Result> bachelorFirst;

    public List<Result> bachelorSecondLong;

    public List<Result> bachelorSecondShort;

    public List<Result> masterFirst;

    public List<Result> masterSecondLong;

    public List<Result> masterSecondShort;

    public List<LocalDate> practicalCheck;

    public List<Result> practicalThesesFirst;

    public List<Result> practicalThesesSecondLong;

    public List<Result> practicalThesesSecondShort;

    public int sum() {
        return this.bachelorFirst.size() * 3
            + this.bachelorSecondShort.size()
            + this.bachelorSecondLong.size() * 2
            + this.masterFirst.size() * 5
            + this.masterSecondShort.size()
            + this.masterSecondLong.size() * 3
            + this.practicalCheck.size()
            + this.practicalThesesFirst.size() * 2
            + (this.practicalThesesSecondShort.size() + this.practicalThesesSecondLong.size()) / 2;
    }

}
