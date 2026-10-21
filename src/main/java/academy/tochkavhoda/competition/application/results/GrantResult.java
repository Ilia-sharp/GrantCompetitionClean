package academy.tochkavhoda.competition.application.results;

public final class GrantResult {
    private final int applicationId;
    private final String title;
    private final String companyName;
    private final double averageRating;
    private final long grantSum;

    public GrantResult(int applicationId, String title, String companyName, double averageRating, long grantSum) {
        this.applicationId = applicationId;
        this.title = title;
        this.companyName = companyName;
        this.averageRating = averageRating;
        this.grantSum = grantSum;
    }

    public int getApplicationId() { return applicationId; }
    public String getTitle() { return title; }
    public String getCompanyName() { return companyName; }
    public double getAverageRating() { return averageRating; }
    public long getGrantSum() { return grantSum; }
}
