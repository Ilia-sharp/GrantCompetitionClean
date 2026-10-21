package academy.tochkavhoda.competition.application.results;

public final class RatedApplicationResult {
    private final ApplicationResult application;
    private final int rating;

    public RatedApplicationResult(ApplicationResult application, int rating) {
        this.application = application;
        this.rating = rating;
    }

    public ApplicationResult getApplication() { return application; }
    public int getRating() { return rating; }
}
