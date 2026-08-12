package theseshelper;

import java.io.*;
import java.lang.reflect.*;
import java.nio.charset.*;
import java.util.logging.*;

import com.google.gson.*;

import clit.*;
import theseshelper.review.*;

public class Main {

    public static final Gson GSON;

    public static final Logger LOGGER;

    public static final Charset UTF8;

    static {
        LOGGER = LogManager.getLogManager().getLogger("");
        final StreamHandler handler = new StreamHandler(System.out, new SimpleFormatter());
        handler.setLevel(Level.ALL);
        Main.LOGGER.addHandler(handler);
        GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        UTF8 = Charset.forName("UTF-8");
    }

    public static void main(final String[] args)
    throws IOException, InterruptedException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        final CLITamer<Flag> tamer = new CLITamer<Flag>(Flag.class);
        if (args == null || args.length < 1) {
            System.out.println(tamer.getParameterDescriptions());
            return;
        }
        if (args.length == 1 && "-s".equals(args[0])) {
            Main.support(new File(System.getProperty("user.dir")));
            return;
        }
        final Parameters<Flag> options = tamer.parse(args);
        if (options.containsKey(Flag.HELP)) {
            System.out.println(Mode.valueOf(options.get(Flag.HELP)).help());
            return;
        }
        if (options.getAsBooleanOrDefault(Flag.VERBOSITY, false)) {
            Main.LOGGER.setLevel(Level.ALL);
        } else {
            Main.LOGGER.setLevel(Level.WARNING);
        }
        final File root = options.containsKey(Flag.DIRECTORY) ? new File(options.get(Flag.DIRECTORY)) : null;
        Main.LOGGER.log(Level.FINE, "Root file: " + (root == null ? "NONE" : root.getAbsolutePath()));
        final int year = Integer.parseInt(options.getOrDefault(Flag.YEAR, "0"));
        Main.LOGGER.log(Level.FINE, "Year: " + year);
        final Years years = new Years(year);
        switch (Mode.valueOf(options.get(Flag.MODE))) {
        case STATISTICS:
            Main.LOGGER.log(Level.FINE, "Computing statistics...");
            if (options.containsKey(Flag.REVIEWER_TYPE)) {
                final ReviewerType reviewerType = ReviewerType.valueOf(options.get(Flag.REVIEWER_TYPE));
                final ThesisType thesisType = ThesisType.valueOf(options.getOrDefault(Flag.THESIS_TYPE, "ALL"));
                StatisticsWriter.statistics(root, reviewerType, thesisType, years);
            } else {
                for (final ReviewerType reviewerType : ReviewerType.values()) {
                    for (final ThesisType thesisType : ThesisType.values()) {
                        StatisticsWriter.statistics(root, reviewerType, thesisType, years);
                    }
                }
            }
            return;
        case POINTS:
            Main.LOGGER.log(Level.FINE, "Calculating POINTS...");
            PointsWriter.writePoints(root, year);
            return;
        case PREPARATION:
            Main.LOGGER.log(Level.FINE, "Preparing reviews...");
            ReviewPreparator.prepare(root, year);
            return;
        case SPELLCHECK:
            Main.LOGGER.log(Level.FINE, "Spellchecking theses...");
            SpellChecker.spellcheck(root, year);
            return;
        case REVIEW:
            Main.LOGGER.log(Level.FINE, "Creating review tex file...");
            ReviewWriter.write(new File(options.get(Flag.INPUT)));
            return;
        case FINISH:
            Main.LOGGER.log(Level.FINE, "Finishing review...");
            ReviewFinisher.finish(new File(options.get(Flag.INPUT)));
            return;
        case UNFINISHED:
            Main.LOGGER.log(Level.FINE, "Computing unfinished reviews...");
            new UnfinishedSubmissions(root, years).write();
            return;
        case CRITERIA:
            Main.LOGGER.log(Level.FINE, "Pretty printing criteria file...");
            final File criteriaFile = new File(options.get(Flag.INPUT));
            final CriteriaRaw criteria = Criteria.parseCriteria(criteriaFile).toRaw();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(criteriaFile, Main.UTF8))) {
                Main.GSON.toJson(criteria, criteria.getClass(), writer);
            }
            return;
        default:
            throw new IllegalStateException("Unknown Mode detected!");
        }
    }

    private static void support(final File root) throws IOException {
        for (int year = 2022; year <= 2026; year++) {
            for (final File resultFile : ResultFileFinder.findAllResultFiles(root, year)) {
                System.out.println(resultFile);
            }
        }
    }

}
