package academy.tochkavhoda.competition.application.results;

import java.util.ArrayList;
import java.util.List;

public final class CompetitionResultResult {
    private final long fund;
    private final double threshold;
    private final long distributedSum;
    private final long remainingSum;
    private final List<GrantResult> grants;

    public CompetitionResultResult(long fund, double threshold, long distributedSum, long remainingSum, List<GrantResult> grants) {
        this.fund = fund;
        this.threshold = threshold;
        this.distributedSum = distributedSum;
        this.remainingSum = remainingSum;
        this.grants = new ArrayList<>(grants);
    }

    public long getFund() { return fund; }
    public double getThreshold() { return threshold; }
    public long getDistributedSum() { return distributedSum; }
    public long getRemainingSum() { return remainingSum; }
    public List<GrantResult> getGrants() { return new ArrayList<>(grants); }
}
