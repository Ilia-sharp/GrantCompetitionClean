package academy.tochkavhoda.competition.api.dto.responses;

public class GrantResponse {
    private int applicationId;
    private String title;
    private String companyName;
    private double averageRating;
    private long grantSum;

    public static GrantResponse from(academy.tochkavhoda.competition.application.results.GrantResult result) {
        return new GrantResponse(result.getApplicationId(), result.getTitle(), result.getCompanyName(), result.getAverageRating(), result.getGrantSum());
    }

    public GrantResponse() {
    }

    public GrantResponse(int applicationId, String title, String companyName, double averageRating, long grantSum) {
        this.applicationId = applicationId;
        this.title = title;
        this.companyName = companyName;
        this.averageRating = averageRating;
        this.grantSum = grantSum;
    }

    public int getApplicationId() { return applicationId; }
    public void setApplicationId(int applicationId) { this.applicationId = applicationId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public double getAverageRating() { return averageRating; }
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    public long getGrantSum() { return grantSum; }
    public void setGrantSum(long grantSum) { this.grantSum = grantSum; }
}
