package academy.tochkavhoda.competition.application.commands;

import java.util.ArrayList;
import java.util.List;

public final class CreateApplicationCommand {
    private final String title;
    private final String description;
    private final List<String> directions;
    private final Long requestedSum;

    public CreateApplicationCommand(String title, String description, List<String> directions, Long requestedSum) {
        this.title = title;
        this.description = description;
        this.directions = directions == null ? null : new ArrayList<>(directions);
        this.requestedSum = requestedSum;
    }

    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public List<String> getDirections() { return directions == null ? null : new ArrayList<>(directions); }
    public Long getRequestedSum() { return requestedSum; }
}
