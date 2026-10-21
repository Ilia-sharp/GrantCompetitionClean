package academy.tochkavhoda.competition.server;

import academy.tochkavhoda.competition.api.dto.responses.ServerResponse;
import academy.tochkavhoda.competition.application.ports.ApplicationDao;
import academy.tochkavhoda.competition.application.ports.CompetitionDao;
import academy.tochkavhoda.competition.application.ports.DebugDao;
import academy.tochkavhoda.competition.application.ports.RatingDao;
import academy.tochkavhoda.competition.application.ports.UserDao;
import academy.tochkavhoda.competition.application.services.ApplicationService;
import academy.tochkavhoda.competition.application.services.CompetitionService;
import academy.tochkavhoda.competition.application.services.MaintenanceService;
import academy.tochkavhoda.competition.application.services.RatingService;
import academy.tochkavhoda.competition.application.services.UserService;
import academy.tochkavhoda.competition.infrastructure.persistence.ApplicationDaoImpl;
import academy.tochkavhoda.competition.infrastructure.persistence.CompetitionDaoImpl;
import academy.tochkavhoda.competition.infrastructure.persistence.DebugDaoImpl;
import academy.tochkavhoda.competition.infrastructure.persistence.RatingDaoImpl;
import academy.tochkavhoda.competition.infrastructure.persistence.UserDaoImpl;

public final class Server {
    private final academy.tochkavhoda.competition.api.Server apiServer;

    public Server() {
        UserDao userDao = new UserDaoImpl();
        ApplicationDao applicationDao = new ApplicationDaoImpl();
        RatingDao ratingDao = new RatingDaoImpl();
        CompetitionDao competitionDao = new CompetitionDaoImpl();
        DebugDao debugDao = new DebugDaoImpl();

        UserService userService = new UserService(userDao, applicationDao);
        ApplicationService applicationService = new ApplicationService(userDao, applicationDao);
        RatingService ratingService = new RatingService(userDao, applicationDao, ratingDao);
        CompetitionService competitionService = new CompetitionService(userDao, competitionDao);
        MaintenanceService maintenanceService = new MaintenanceService(debugDao);

        apiServer = new academy.tochkavhoda.competition.api.Server(
                userService, applicationService, ratingService, competitionService, maintenanceService);
    }

    public ServerResponse clearDatabase() {
        return apiServer.clearDatabase();
    }

    public ServerResponse registerParticipant(String requestJsonString) {
        return apiServer.registerParticipant(requestJsonString);
    }

    public ServerResponse registerExpert(String requestJsonString) {
        return apiServer.registerExpert(requestJsonString);
    }

    public ServerResponse login(String requestJsonString) {
        return apiServer.login(requestJsonString);
    }

    public ServerResponse logout(String token) {
        return apiServer.logout(token);
    }

    public ServerResponse leaveServer(String token) {
        return apiServer.leaveServer(token);
    }

    public ServerResponse getParticipants(String token, String requestJsonString) {
        return apiServer.getParticipants(token, requestJsonString);
    }

    public ServerResponse addApplication(String token, String requestJsonString) {
        return apiServer.addApplication(token, requestJsonString);
    }

    public ServerResponse addApplications(String token, String requestJsonString) {
        return apiServer.addApplications(token, requestJsonString);
    }

    public ServerResponse removeApplication(String token, String requestJsonString) {
        return apiServer.removeApplication(token, requestJsonString);
    }

    public ServerResponse getMyApplications(String token) {
        return apiServer.getMyApplications(token);
    }

    public ServerResponse getApplicationsByDirections(String token, String requestJsonString) {
        return apiServer.getApplicationsByDirections(token, requestJsonString);
    }

    public ServerResponse rateApplication(String token, String requestJsonString) {
        return apiServer.rateApplication(token, requestJsonString);
    }

    public ServerResponse changeRating(String token, String requestJsonString) {
        return apiServer.changeRating(token, requestJsonString);
    }

    public ServerResponse deleteRating(String token, String requestJsonString) {
        return apiServer.deleteRating(token, requestJsonString);
    }

    public ServerResponse getRatedApplications(String token) {
        return apiServer.getRatedApplications(token);
    }

    public ServerResponse summarize(String token, String requestJsonString) {
        return apiServer.summarize(token, requestJsonString);
    }

    public ServerResponse getResults(String token) {
        return apiServer.getResults(token);
    }
}
