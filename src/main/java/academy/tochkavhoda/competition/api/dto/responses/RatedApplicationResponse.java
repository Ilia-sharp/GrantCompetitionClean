package academy.tochkavhoda.competition.api.dto.responses;

public class RatedApplicationResponse {
    private ApplicationResponse application;
    private int rating;

    public static RatedApplicationResponse from(academy.tochkavhoda.competition.application.results.RatedApplicationResult result) {
        return new RatedApplicationResponse(ApplicationResponse.from(result.getApplication()), result.getRating());
    }

    public RatedApplicationResponse() {
    }

    public RatedApplicationResponse(ApplicationResponse application, int rating) {
        this.application = application;
        this.rating = rating;
    }

    public ApplicationResponse getApplication() { return application; }
    public void setApplication(ApplicationResponse application) { this.application = application; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
}
