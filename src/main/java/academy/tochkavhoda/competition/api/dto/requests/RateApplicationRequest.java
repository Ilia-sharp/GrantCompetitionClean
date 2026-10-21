package academy.tochkavhoda.competition.api.dto.requests;

public class RateApplicationRequest {
    private Integer applicationId;
    private Integer rating;

    public RateApplicationRequest() {
    }

    public RateApplicationRequest(Integer applicationId, Integer rating) {
        this.applicationId = applicationId;
        this.rating = rating;
    }

    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }

    public Integer getRating() { return rating; }
    public void setRating(Integer rating) { this.rating = rating; }
}
