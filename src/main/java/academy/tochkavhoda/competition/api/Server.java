package academy.tochkavhoda.competition.api;

import academy.tochkavhoda.competition.api.dto.requests.AddApplicationRequest;
import academy.tochkavhoda.competition.api.dto.requests.AddApplicationsRequest;
import academy.tochkavhoda.competition.api.dto.requests.ApplicationIdRequest;
import academy.tochkavhoda.competition.api.dto.requests.DirectionsRequest;
import academy.tochkavhoda.competition.api.dto.requests.LoginRequest;
import academy.tochkavhoda.competition.api.dto.requests.RateApplicationRequest;
import academy.tochkavhoda.competition.api.dto.requests.RegisterExpertRequest;
import academy.tochkavhoda.competition.api.dto.requests.RegisterParticipantRequest;
import academy.tochkavhoda.competition.api.dto.requests.SummarizeRequest;
import academy.tochkavhoda.competition.api.dto.responses.AddApplicationResponse;
import academy.tochkavhoda.competition.api.dto.responses.AddApplicationsResponse;
import academy.tochkavhoda.competition.api.dto.responses.ApplicationResponse;
import academy.tochkavhoda.competition.api.dto.responses.ApplicationsResponse;
import academy.tochkavhoda.competition.api.dto.responses.CompetitionResultResponse;
import academy.tochkavhoda.competition.api.dto.responses.EmptyResponse;
import academy.tochkavhoda.competition.api.dto.responses.ErrorResponse;
import academy.tochkavhoda.competition.api.dto.responses.LoginResponse;
import academy.tochkavhoda.competition.api.dto.responses.ParticipantResponse;
import academy.tochkavhoda.competition.api.dto.responses.ParticipantsResponse;
import academy.tochkavhoda.competition.api.dto.responses.RatedApplicationResponse;
import academy.tochkavhoda.competition.api.dto.responses.RatedApplicationsResponse;
import academy.tochkavhoda.competition.api.dto.responses.ServerResponse;
import academy.tochkavhoda.competition.application.commands.CreateApplicationCommand;
import academy.tochkavhoda.competition.application.results.ApplicationResult;
import academy.tochkavhoda.competition.application.results.ParticipantResult;
import academy.tochkavhoda.competition.application.results.RatedApplicationResult;
import academy.tochkavhoda.competition.application.services.ApplicationService;
import academy.tochkavhoda.competition.application.services.CompetitionService;
import academy.tochkavhoda.competition.application.services.MaintenanceService;
import academy.tochkavhoda.competition.application.services.RatingService;
import academy.tochkavhoda.competition.application.services.UserService;
import academy.tochkavhoda.competition.exception.ServerErrorCode;
import academy.tochkavhoda.competition.exception.ServerException;
import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.util.ArrayList;
import java.util.List;

public class Server {
    private static final Gson GSON = new Gson();

    private final UserService userService;
    private final ApplicationService applicationService;
    private final RatingService ratingService;
    private final CompetitionService competitionService;
    private final MaintenanceService maintenanceService;

    public Server(UserService userService, ApplicationService applicationService,
                  RatingService ratingService, CompetitionService competitionService,
                  MaintenanceService maintenanceService) {
        this.userService = userService;
        this.applicationService = applicationService;
        this.ratingService = ratingService;
        this.competitionService = competitionService;
        this.maintenanceService = maintenanceService;
    }

    public ServerResponse clearDatabase() {
        return handle(() -> {
            maintenanceService.clearDatabase();
            return new EmptyResponse();
        });
    }

    public ServerResponse registerParticipant(String requestJsonString) {
        return handle(() -> {
            RegisterParticipantRequest request = parse(requestJsonString, RegisterParticipantRequest.class);
            userService.registerParticipant(request.getFirstName(), request.getLastName(), request.getCompanyName(),
                    request.getLogin(), request.getPassword());
            return new EmptyResponse();
        });
    }

    public ServerResponse registerExpert(String requestJsonString) {
        return handle(() -> {
            RegisterExpertRequest request = parse(requestJsonString, RegisterExpertRequest.class);
            userService.registerExpert(request.getFirstName(), request.getLastName(), request.getDirections(),
                    request.getLogin(), request.getPassword());
            return new EmptyResponse();
        });
    }

    public ServerResponse login(String requestJsonString) {
        return handle(() -> {
            LoginRequest request = parse(requestJsonString, LoginRequest.class);
            return new LoginResponse(userService.login(request.getLogin(), request.getPassword()));
        });
    }

    public ServerResponse logout(String token) {
        return handle(() -> {
            userService.logout(token);
            return new EmptyResponse();
        });
    }

    public ServerResponse leaveServer(String token) {
        return handle(() -> {
            userService.leaveServer(token);
            return new EmptyResponse();
        });
    }

    public ServerResponse getParticipants(String token, String requestJsonString) {
        return handle(() -> {
            DirectionsRequest request = parseOptional(requestJsonString, DirectionsRequest.class, new DirectionsRequest());
            List<ParticipantResponse> participants = new ArrayList<>();
            for (ParticipantResult result : userService.getParticipants(token, request.getDirections())) {
                participants.add(ParticipantResponse.from(result));
            }
            return new ParticipantsResponse(participants);
        });
    }

    public ServerResponse addApplication(String token, String requestJsonString) {
        return handle(() -> {
            AddApplicationRequest request = parse(requestJsonString, AddApplicationRequest.class);
            CreateApplicationCommand command = toCommand(request);
            return new AddApplicationResponse(applicationService.addApplication(token, command));
        });
    }

    public ServerResponse addApplications(String token, String requestJsonString) {
        return handle(() -> {
            AddApplicationsRequest request = parse(requestJsonString, AddApplicationsRequest.class);
            List<CreateApplicationCommand> commands = new ArrayList<>();
            if (request.getApplications() != null) {
                for (AddApplicationRequest item : request.getApplications()) {
                    if (item == null) {
                        throw new ServerException(ServerErrorCode.WRONG_JSON);
                    }
                    commands.add(toCommand(item));
                }
            }
            return new AddApplicationsResponse(applicationService.addApplications(token, commands));
        });
    }

    public ServerResponse removeApplication(String token, String requestJsonString) {
        return handle(() -> {
            ApplicationIdRequest request = parse(requestJsonString, ApplicationIdRequest.class);
            applicationService.removeApplication(token, request.getApplicationId());
            return new EmptyResponse();
        });
    }

    public ServerResponse getMyApplications(String token) {
        return handle(() -> new ApplicationsResponse(toApplicationResponses(applicationService.getMyApplications(token))));
    }

    public ServerResponse getApplicationsByDirections(String token, String requestJsonString) {
        return handle(() -> {
            DirectionsRequest request = parseOptional(requestJsonString, DirectionsRequest.class, new DirectionsRequest());
            return new ApplicationsResponse(toApplicationResponses(
                    applicationService.getApplicationsByDirections(token, request.getDirections())));
        });
    }

    public ServerResponse rateApplication(String token, String requestJsonString) {
        return handle(() -> {
            RateApplicationRequest request = parse(requestJsonString, RateApplicationRequest.class);
            ratingService.rateApplication(token, request.getApplicationId(), request.getRating());
            return new EmptyResponse();
        });
    }

    public ServerResponse changeRating(String token, String requestJsonString) {
        return handle(() -> {
            RateApplicationRequest request = parse(requestJsonString, RateApplicationRequest.class);
            ratingService.changeRating(token, request.getApplicationId(), request.getRating());
            return new EmptyResponse();
        });
    }

    public ServerResponse deleteRating(String token, String requestJsonString) {
        return handle(() -> {
            ApplicationIdRequest request = parse(requestJsonString, ApplicationIdRequest.class);
            ratingService.deleteRating(token, request.getApplicationId());
            return new EmptyResponse();
        });
    }

    public ServerResponse getRatedApplications(String token) {
        return handle(() -> {
            List<RatedApplicationResponse> applications = new ArrayList<>();
            for (RatedApplicationResult result : ratingService.getRatedApplications(token)) {
                applications.add(RatedApplicationResponse.from(result));
            }
            return new RatedApplicationsResponse(applications);
        });
    }

    public ServerResponse summarize(String token, String requestJsonString) {
        return handle(() -> {
            SummarizeRequest request = parse(requestJsonString, SummarizeRequest.class);
            return CompetitionResultResponse.from(
                    competitionService.summarize(token, request.getFund(), request.getThreshold()));
        });
    }

    public ServerResponse getResults(String token) {
        return handle(() -> CompetitionResultResponse.from(competitionService.getResults(token)));
    }

    private static CreateApplicationCommand toCommand(AddApplicationRequest request) {
        return new CreateApplicationCommand(request.getTitle(), request.getDescription(),
                request.getDirections(), request.getRequestedSum());
    }

    private static List<ApplicationResponse> toApplicationResponses(List<ApplicationResult> results) {
        List<ApplicationResponse> responses = new ArrayList<>();
        for (ApplicationResult result : results) {
            responses.add(ApplicationResponse.from(result));
        }
        return responses;
    }

    private static <T> T parse(String json, Class<T> type) throws ServerException {
        if (json == null || json.isBlank()) {
            throw new ServerException(ServerErrorCode.WRONG_JSON);
        }
        try {
            T value = GSON.fromJson(json, type);
            if (value == null) {
                throw new ServerException(ServerErrorCode.WRONG_JSON);
            }
            return value;
        } catch (JsonParseException | IllegalStateException | NumberFormatException exception) {
            throw new ServerException(ServerErrorCode.WRONG_JSON);
        }
    }

    private static <T> T parseOptional(String json, Class<T> type, T emptyValue) throws ServerException {
        if (json == null || json.isBlank()) {
            return emptyValue;
        }
        return parse(json, type);
    }

    private static ServerResponse handle(ApiAction action) {
        try {
            Object body = action.run();
            return new ServerResponse(ServerResponse.CODE_SUCCESS, GSON.toJson(body));
        } catch (ServerException exception) {
            return new ServerResponse(ServerResponse.CODE_ERROR, GSON.toJson(new ErrorResponse(exception.getMessage())));
        }
    }

    @FunctionalInterface
    private interface ApiAction {
        Object run() throws ServerException;
    }
}
