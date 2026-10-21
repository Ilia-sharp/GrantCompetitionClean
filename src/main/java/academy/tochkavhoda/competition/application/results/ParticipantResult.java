package academy.tochkavhoda.competition.application.results;

import academy.tochkavhoda.competition.domain.models.Participant;

public final class ParticipantResult {
    private final String firstName;
    private final String lastName;
    private final String companyName;

    public ParticipantResult(String firstName, String lastName, String companyName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
    }

    public static ParticipantResult from(Participant participant) {
        return new ParticipantResult(participant.getFirstName(), participant.getLastName(), participant.getCompanyName());
    }

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getCompanyName() { return companyName; }
}
