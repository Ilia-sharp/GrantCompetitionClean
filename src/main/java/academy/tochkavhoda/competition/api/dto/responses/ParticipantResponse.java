package academy.tochkavhoda.competition.api.dto.responses;

public class ParticipantResponse {
    private String firstName;
    private String lastName;
    private String companyName;

    public static ParticipantResponse from(academy.tochkavhoda.competition.application.results.ParticipantResult result) {
        return new ParticipantResponse(result.getFirstName(), result.getLastName(), result.getCompanyName());
    }

    public ParticipantResponse() {
    }

    public ParticipantResponse(String firstName, String lastName, String companyName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }
}
