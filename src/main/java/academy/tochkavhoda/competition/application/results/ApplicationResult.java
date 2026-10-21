package academy.tochkavhoda.competition.application.results;

import academy.tochkavhoda.competition.domain.models.GrantApplication;

import java.util.ArrayList;
import java.util.List;

public final class ApplicationResult {
    private final int id;
    private final String title;
    private final String description;
    private final List<String> directions;
    private final long requestedSum;
    private final String companyName;

    public ApplicationResult(int id, String title, String description, List<String> directions, long requestedSum, String companyName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.directions = new ArrayList<>(directions);
        this.requestedSum = requestedSum;
        this.companyName = companyName;
    }

    public static ApplicationResult from(GrantApplication application) {
        return new ApplicationResult(application.getId(), application.getTitle(), application.getDescription(),
                new ArrayList<>(application.getDirections()), application.getRequestedSum(),
                application.getParticipant().getCompanyName());
    }

    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getDirections() { return new ArrayList<>(directions); }
    public long getRequestedSum() { return requestedSum; }
    public String getCompanyName() { return companyName; }
}
