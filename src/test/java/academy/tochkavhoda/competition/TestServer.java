package academy.tochkavhoda.competition;

import academy.tochkavhoda.competition.server.Server;
import academy.tochkavhoda.competition.api.dto.responses.AddApplicationResponse;
import academy.tochkavhoda.competition.api.dto.responses.CompetitionResultResponse;
import academy.tochkavhoda.competition.api.dto.responses.LoginResponse;
import academy.tochkavhoda.competition.api.dto.responses.ServerResponse;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TestServer {
    private final Gson gson = new Gson();
    private Server server;

    @BeforeEach
    void setUp() {
        server = new Server();
        server.clearDatabase();
    }

    @Test
    void participantCanRegisterSubmitApplicationAndLeave() {
        assertEquals(200, server.registerParticipant("""
                {"firstName":"Anna","lastName":"Ivanova","companyName":"Green Lab","login":"anna1","password":"Password1!"}
                """).getResponseCode());

        String token = login("anna1", "Password1!");
        ServerResponse added = server.addApplication(token, """
                {"title":"Smart greenhouse","description":"Automated watering","directions":["biology"],"requestedSum":250}
                """);
        assertEquals(200, added.getResponseCode());
        AddApplicationResponse application = gson.fromJson(added.getResponseData(), AddApplicationResponse.class);
        assertEquals(1, application.getId());
        assertEquals(200, server.leaveServer(token).getResponseCode());
        assertEquals(400, server.getMyApplications(token).getResponseCode());
    }

    @Test
    void expertCanRateApplicationAndFinalizeCompetition() {
        assertEquals(200, server.registerParticipant("""
                {"firstName":"Anna","lastName":"Ivanova","companyName":"Green Lab","login":"anna1","password":"Password1!"}
                """).getResponseCode());
        assertEquals(200, server.registerExpert("""
                {"firstName":"Petr","lastName":"Petrov","directions":["biology"],"login":"expert1","password":"Password1!"}
                """).getResponseCode());

        String participantToken = login("anna1", "Password1!");
        String expertToken = login("expert1", "Password1!");
        ServerResponse added = server.addApplication(participantToken, """
                {"title":"Smart greenhouse","description":"Automated watering","directions":["biology"],"requestedSum":250}
                """);
        int id = gson.fromJson(added.getResponseData(), AddApplicationResponse.class).getId();

        assertEquals(200, server.rateApplication(expertToken, "{\"applicationId\":" + id + ",\"rating\":5}").getResponseCode());
        ServerResponse summary = server.summarize(expertToken, "{\"fund\":500,\"threshold\":0}");
        assertEquals(200, summary.getResponseCode());
        CompetitionResultResponse result = gson.fromJson(summary.getResponseData(), CompetitionResultResponse.class);
        assertEquals(250, result.getDistributedSum());
        assertEquals(250, result.getRemainingSum());
        assertEquals(1, result.getGrants().size());
        assertEquals(200, server.getResults(participantToken).getResponseCode());
        assertEquals(400, server.rateApplication(expertToken, "{\"applicationId\":" + id + ",\"rating\":4}").getResponseCode());
    }

    @Test
    void loginIssuesNewTokenAndOldTokenBecomesInvalid() {
        assertEquals(200, server.registerParticipant("""
                {"firstName":"Anna","lastName":"Ivanova","companyName":"Green Lab","login":"anna1","password":"Password1!"}
                """).getResponseCode());
        String first = login("anna1", "Password1!");
        String second = login("anna1", "Password1!");
        assertNotEquals(first, second);
        assertEquals(400, server.getMyApplications(first).getResponseCode());
        assertEquals(200, server.getMyApplications(second).getResponseCode());
    }

    @Test
    void errorsUseTheSameServerResponseFormat() {
        ServerResponse badJson = server.registerParticipant("not-json");
        assertEquals(400, badJson.getResponseCode());
        assertNotNull(badJson.getResponseData());
        assertEquals(400, server.logout("missing-token").getResponseCode());
        assertEquals(200, server.registerParticipant("""
                {"firstName":"Anna","lastName":"Ivanova","companyName":"Green Lab","login":"anna1","password":"Password1!"}
                """).getResponseCode());
        assertEquals(400, server.registerParticipant("""
                {"firstName":"Other","lastName":"Person","companyName":"Other Lab","login":"anna1","password":"Password2!"}
                """).getResponseCode());
    }

    private String login(String login, String password) {
        ServerResponse response = server.login("{\"login\":\"" + login + "\",\"password\":\"" + password + "\"}");
        assertEquals(200, response.getResponseCode());
        return gson.fromJson(response.getResponseData(), LoginResponse.class).getToken();
    }
}
