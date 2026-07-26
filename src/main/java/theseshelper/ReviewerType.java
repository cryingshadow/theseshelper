package theseshelper;

public enum ReviewerType {

    ALL("Betreuer"), FIRST("Erstbetreuer"), SECOND("Zweitbetreuer");

    public final String title;

    private ReviewerType(final String title) {
        this.title = title;
    }

}
