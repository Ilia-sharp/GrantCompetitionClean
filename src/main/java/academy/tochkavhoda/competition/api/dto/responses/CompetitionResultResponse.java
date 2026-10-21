package academy.tochkavhoda.competition.api.dto.responses;

import java.util.List;

public class CompetitionResultResponse {
    private long fund;
    private double threshold;
    private long distributedSum;
    private long remainingSum;
    private List<GrantResponse> grants;

    public static CompetitionResultResponse from(academy.tochkavhoda.competition.application.results.CompetitionResultResult result) {
        java.util.List<GrantResponse> grants = new java.util.ArrayList<>();
        for (academy.tochkavhoda.competition.application.results.GrantResult grant : result.getGrants()) {
            grants.add(GrantResponse.from(grant));
        }
        return new CompetitionResultResponse(result.getFund(), result.getThreshold(), result.getDistributedSum(), result.getRemainingSum(), grants);
    }

    public CompetitionResultResponse() {
    }

    public CompetitionResultResponse(long fund, double threshold, long distributedSum, long remainingSum, List<GrantResponse> grants) {
        this.fund = fund;
        this.threshold = threshold;
        this.distributedSum = distributedSum;
        this.remainingSum = remainingSum;
        this.grants = grants;
    }

    public long getFund() { return fund; }
    public void setFund(long fund) { this.fund = fund; }

    public double getThreshold() { return threshold; }
    public void setThreshold(double threshold) { this.threshold = threshold; }

    public long getDistributedSum() { return distributedSum; }
    public void setDistributedSum(long distributedSum) { this.distributedSum = distributedSum; }

    public long getRemainingSum() { return remainingSum; }
    public void setRemainingSum(long remainingSum) { this.remainingSum = remainingSum; }

    public List<GrantResponse> getGrants() { return grants; }
    public void setGrants(List<GrantResponse> grants) { this.grants = grants; }
}
