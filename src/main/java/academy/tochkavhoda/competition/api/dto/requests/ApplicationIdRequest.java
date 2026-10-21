package academy.tochkavhoda.competition.api.dto.requests;

public class ApplicationIdRequest {
    private Integer applicationId;

    public ApplicationIdRequest() {
    }

    public ApplicationIdRequest(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getApplicationId() { return applicationId; }
    public void setApplicationId(Integer applicationId) { this.applicationId = applicationId; }
}
