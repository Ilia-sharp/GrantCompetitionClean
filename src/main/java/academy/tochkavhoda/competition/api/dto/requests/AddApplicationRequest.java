package academy.tochkavhoda.competition.api.dto.requests;

import java.util.List;

public class AddApplicationRequest {
    private String title;
    private String description;
    private List<String> directions;
    private Long requestedSum;

    public AddApplicationRequest() {
    }

    public AddApplicationRequest(String title, String description, List<String> directions, Long requestedSum) {
        this.title = title;
        this.description = description;
        this.directions = directions;
        this.requestedSum = requestedSum;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public List<String> getDirections() { return directions; }
    public void setDirections(List<String> directions) { this.directions = directions; }

    public Long getRequestedSum() { return requestedSum; }
    public void setRequestedSum(Long requestedSum) { this.requestedSum = requestedSum; }
}
