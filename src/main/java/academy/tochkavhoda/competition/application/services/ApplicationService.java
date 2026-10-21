package academy.tochkavhoda.competition.application.services;

import academy.tochkavhoda.competition.application.commands.CreateApplicationCommand;
import academy.tochkavhoda.competition.application.ports.ApplicationDao;
import academy.tochkavhoda.competition.application.ports.UserDao;
import academy.tochkavhoda.competition.application.results.ApplicationResult;
import academy.tochkavhoda.competition.domain.models.Expert;
import academy.tochkavhoda.competition.domain.models.GrantApplication;
import academy.tochkavhoda.competition.domain.models.Participant;
import academy.tochkavhoda.competition.exception.ServerErrorCode;
import academy.tochkavhoda.competition.exception.ServerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static academy.tochkavhoda.competition.application.services.ServiceSupport.getExpertByToken;
import static academy.tochkavhoda.competition.application.services.ServiceSupport.getParticipantByToken;
import static academy.tochkavhoda.competition.application.services.ServiceSupport.isBlank;
import static academy.tochkavhoda.competition.application.services.ServiceSupport.normalizeDirections;

public final class ApplicationService {
    private static final int MAX_TITLE_LENGTH = 200;

    private final UserDao userDao;
    private final ApplicationDao applicationDao;

    public ApplicationService(UserDao userDao, ApplicationDao applicationDao) {
        this.userDao = userDao;
        this.applicationDao = applicationDao;
    }

    public int addApplication(String token, CreateApplicationCommand command) throws ServerException {
        Participant participant = getParticipantByToken(userDao, token);
        GrantApplication application = createApplication(participant, command);
        return applicationDao.insert(Collections.singletonList(application)).get(0);
    }

    public List<Integer> addApplications(String token, List<CreateApplicationCommand> commands) throws ServerException {
        Participant participant = getParticipantByToken(userDao, token);
        if (commands == null || commands.isEmpty()) {
            throw new ServerException(ServerErrorCode.EMPTY_APPLICATION_LIST);
        }
        List<GrantApplication> applications = new ArrayList<>();
        for (CreateApplicationCommand command : commands) {
            if (command == null) {
                throw new ServerException(ServerErrorCode.WRONG_JSON);
            }
            applications.add(createApplication(participant, command));
        }
        return applicationDao.insert(applications);
    }

    public void removeApplication(String token, Integer applicationId) throws ServerException {
        Participant participant = getParticipantByToken(userDao, token);
        if (applicationId == null) {
            throw new ServerException(ServerErrorCode.WRONG_APPLICATION_ID);
        }
        GrantApplication application = applicationDao.getById(applicationId);
        if (application == null) {
            throw new ServerException(ServerErrorCode.APPLICATION_NOT_FOUND, applicationId);
        }
        if (!application.getParticipant().equals(participant)) {
            throw new ServerException(ServerErrorCode.NOT_APPLICATION_OWNER, applicationId);
        }
        applicationDao.delete(application);
    }

    public List<ApplicationResult> getMyApplications(String token) throws ServerException {
        Participant participant = getParticipantByToken(userDao, token);
        return toResults(applicationDao.getByParticipant(participant));
    }

    public List<ApplicationResult> getApplicationsByDirections(String token, List<String> requestedDirections)
            throws ServerException {
        Expert expert = getExpertByToken(userDao, token);
        Set<String> directions;
        if (requestedDirections == null || requestedDirections.isEmpty()) {
            directions = expert.getDirections();
        } else {
            directions = normalizeDirections(requestedDirections);
            for (String direction : directions) {
                if (!expert.getDirections().contains(direction)) {
                    throw new ServerException(ServerErrorCode.DIRECTION_NOT_EXPERT, direction);
                }
            }
        }
        return toResults(applicationDao.getByDirections(directions));
    }

    private static GrantApplication createApplication(Participant participant, CreateApplicationCommand command)
            throws ServerException {
        validate(command);
        return new GrantApplication(participant, command.getTitle().trim(), command.getDescription().trim(),
                normalizeDirections(command.getDirections()), command.getRequestedSum());
    }

    private static void validate(CreateApplicationCommand command) throws ServerException {
        if (command == null || isBlank(command.getTitle()) || command.getTitle().trim().length() > MAX_TITLE_LENGTH) {
            throw new ServerException(ServerErrorCode.WRONG_TITLE);
        }
        if (isBlank(command.getDescription())) {
            throw new ServerException(ServerErrorCode.WRONG_DESCRIPTION);
        }
        normalizeDirections(command.getDirections());
        if (command.getRequestedSum() == null || command.getRequestedSum() <= 0) {
            throw new ServerException(ServerErrorCode.WRONG_REQUESTED_SUM);
        }
    }

    private static List<ApplicationResult> toResults(List<GrantApplication> applications) {
        List<ApplicationResult> result = new ArrayList<>();
        for (GrantApplication application : applications) {
            result.add(ApplicationResult.from(application));
        }
        return result;
    }
}
