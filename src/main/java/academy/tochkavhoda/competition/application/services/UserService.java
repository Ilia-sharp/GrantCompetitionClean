package academy.tochkavhoda.competition.application.services;

import academy.tochkavhoda.competition.application.ports.ApplicationDao;
import academy.tochkavhoda.competition.application.ports.UserDao;
import academy.tochkavhoda.competition.application.results.ParticipantResult;
import academy.tochkavhoda.competition.domain.models.Expert;
import academy.tochkavhoda.competition.domain.models.GrantApplication;
import academy.tochkavhoda.competition.domain.models.Participant;
import academy.tochkavhoda.competition.domain.models.User;
import academy.tochkavhoda.competition.exception.ServerErrorCode;
import academy.tochkavhoda.competition.exception.ServerException;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static academy.tochkavhoda.competition.application.services.ServiceSupport.getUserByToken;
import static academy.tochkavhoda.competition.application.services.ServiceSupport.isBlank;
import static academy.tochkavhoda.competition.application.services.ServiceSupport.normalizeDirections;

public final class UserService {
    private static final int MAX_NAME_LENGTH = 50;
    private static final int MAX_COMPANY_NAME_LENGTH = 100;
    private static final int MIN_PASSWORD_LENGTH = 8;
    private static final int MAX_PASSWORD_LENGTH = 50;
    private static final Pattern LOGIN_PATTERN = Pattern.compile("[A-Za-z0-9_]{4,30}");

    private final UserDao userDao;
    private final ApplicationDao applicationDao;

    public UserService(UserDao userDao, ApplicationDao applicationDao) {
        this.userDao = userDao;
        this.applicationDao = applicationDao;
    }

    public void registerParticipant(String firstName, String lastName, String companyName, String login, String password)
            throws ServerException {
        validateName(firstName, ServerErrorCode.WRONG_FIRST_NAME, MAX_NAME_LENGTH);
        validateName(lastName, ServerErrorCode.WRONG_LAST_NAME, MAX_NAME_LENGTH);
        validateName(companyName, ServerErrorCode.WRONG_COMPANY_NAME, MAX_COMPANY_NAME_LENGTH);
        validateCredentials(login, password);
        Participant participant = new Participant(firstName.trim(), lastName.trim(), companyName.trim(), login, password);
        userDao.insert(participant);
    }

    public void registerExpert(String firstName, String lastName, List<String> directions, String login, String password)
            throws ServerException {
        validateName(firstName, ServerErrorCode.WRONG_FIRST_NAME, MAX_NAME_LENGTH);
        validateName(lastName, ServerErrorCode.WRONG_LAST_NAME, MAX_NAME_LENGTH);
        Set<String> normalizedDirections = normalizeDirections(directions);
        validateCredentials(login, password);
        userDao.insert(new Expert(firstName.trim(), lastName.trim(), normalizedDirections, login, password));
    }

    public String login(String login, String password) throws ServerException {
        if (isBlank(login) || isBlank(password)) {
            throw new ServerException(ServerErrorCode.WRONG_LOGIN_OR_PASSWORD);
        }
        User user = userDao.getByLogin(login);
        if (user == null || !user.getPassword().equals(password)) {
            throw new ServerException(ServerErrorCode.WRONG_LOGIN_OR_PASSWORD);
        }
        return userDao.login(user);
    }

    public void logout(String token) throws ServerException {
        getUserByToken(userDao, token);
        userDao.logout(token);
    }

    public void leaveServer(String token) throws ServerException {
        User user = getUserByToken(userDao, token);
        userDao.delete(user);
    }

    public List<ParticipantResult> getParticipants(String token, List<String> directions) throws ServerException {
        getUserByToken(userDao, token);
        List<Participant> participants;
        if (directions == null || directions.isEmpty()) {
            participants = userDao.getAllParticipants();
        } else {
            Set<String> normalized = normalizeDirections(directions);
            Set<Participant> selected = new LinkedHashSet<>();
            for (GrantApplication application : applicationDao.getByDirections(normalized)) {
                selected.add(application.getParticipant());
            }
            participants = new ArrayList<>(selected);
        }
        participants.sort(Comparator.comparing(Participant::getCompanyName, String.CASE_INSENSITIVE_ORDER));
        List<ParticipantResult> results = new ArrayList<>();
        for (Participant participant : participants) {
            results.add(ParticipantResult.from(participant));
        }
        return results;
    }

    private static void validateName(String value, ServerErrorCode errorCode, int maxLength) throws ServerException {
        if (isBlank(value) || value.trim().length() > maxLength) {
            throw new ServerException(errorCode);
        }
    }

    private static void validateCredentials(String login, String password) throws ServerException {
        if (login == null || !LOGIN_PATTERN.matcher(login).matches()) {
            throw new ServerException(ServerErrorCode.WRONG_LOGIN);
        }
        if (password == null || password.length() < MIN_PASSWORD_LENGTH || password.length() > MAX_PASSWORD_LENGTH) {
            throw new ServerException(ServerErrorCode.WRONG_PASSWORD);
        }
        boolean hasLetter = false;
        boolean hasDigit = false;
        boolean hasSpecial = false;
        for (char symbol : password.toCharArray()) {
            if (Character.isWhitespace(symbol)) {
                throw new ServerException(ServerErrorCode.WRONG_PASSWORD);
            } else if (Character.isLetter(symbol)) {
                hasLetter = true;
            } else if (Character.isDigit(symbol)) {
                hasDigit = true;
            } else {
                hasSpecial = true;
            }
        }
        if (!hasLetter || !hasDigit || !hasSpecial) {
            throw new ServerException(ServerErrorCode.WRONG_PASSWORD);
        }
    }
}
