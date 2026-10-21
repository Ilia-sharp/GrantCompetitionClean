package academy.tochkavhoda.competition.api.dto.responses;

public class AddApplicationResponse {
    private int id;

    public AddApplicationResponse() {
    }

    public AddApplicationResponse(int id) {
        this.id = id;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
