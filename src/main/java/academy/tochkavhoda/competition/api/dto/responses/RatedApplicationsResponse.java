package academy.tochkavhoda.competition.api.dto.responses;

import java.util.List;

public class RatedApplicationsResponse {
    private List<RatedApplicationResponse> applications;

    public RatedApplicationsResponse() {
    }

    public RatedApplicationsResponse(List<RatedApplicationResponse> applications) {
        this.applications = applications;
    }

    public List<RatedApplicationResponse> getApplications() { return applications; }
    public void setApplications(List<RatedApplicationResponse> applications) { this.applications = applications; }
}
