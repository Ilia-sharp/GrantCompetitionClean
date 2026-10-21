package academy.tochkavhoda.competition.api.dto.requests;

public class SummarizeRequest {
    private Long fund;
    private Double threshold;

    public SummarizeRequest() {
    }

    public SummarizeRequest(Long fund, Double threshold) {
        this.fund = fund;
        this.threshold = threshold;
    }

    public Long getFund() { return fund; }
    public void setFund(Long fund) { this.fund = fund; }

    public Double getThreshold() { return threshold; }
    public void setThreshold(Double threshold) { this.threshold = threshold; }
}
