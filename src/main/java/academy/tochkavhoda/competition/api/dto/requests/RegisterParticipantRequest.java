package academy.tochkavhoda.competition.api.dto.requests;

public class RegisterParticipantRequest {
    private String firstName;
    private String lastName;
    private String companyName;
    private String login;
    private String password;

    public RegisterParticipantRequest() {
    }

    public RegisterParticipantRequest(String firstName, String lastName, String companyName, String login, String password) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.companyName = companyName;
        this.login = login;
        this.password = password;
    }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getLogin() { return login; }
    public void setLogin(String login) { this.login = login; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
