package academy.tochkavhoda.competition.api.dto.responses;

import java.util.List;

public class ApplicationResponse {
    private int id;
    private String title;
    private String description;
    private List<String> directions;
    private long requestedSum;
    private String companyName;

    public static ApplicationResponse from(academy.tochkavhoda.competition.application.results.ApplicationResult result) {
        return new ApplicationResponse(result.getId(), result.getTitle(), result.getDescription(), result.getDirections(), result.getRequestedSum(), result.getCompanyName());
    }

    public ApplicationResponse() {
    }

    public ApplicationResponse(int id, String title, String description, List<String> directions, long requestedSum, String companyName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.directions = directions;
        this.requestedSum = requestedSum;
        this.companyName = companyName;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getDirections() { return directions; }
    public void setDirections(List<String> directions) { this.directions = directions; }

    public long getRequestedSum() { return requestedSum; }
    public void setRequestedSum(long requestedSum) { this.requestedSum = requestedSum; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
