package academy.tochkavhoda.competition.api.dto.responses;

import java.util.List;

public class AddApplicationsResponse {
    private List<Integer> ids;

    public AddApplicationsResponse() {
    }

    public AddApplicationsResponse(List<Integer> ids) {
        this.ids = ids;
    }

    public List<Integer> getIds() { return ids; }
    public void setIds(List<Integer> ids) { this.ids = ids; }
}
