package academy.tochkavhoda.competition.application.services;

import academy.tochkavhoda.competition.application.ports.UserDao;
import academy.tochkavhoda.competition.domain.models.Expert;
import academy.tochkavhoda.competition.domain.models.Participant;
import academy.tochkavhoda.competition.domain.models.User;
import academy.tochkavhoda.competition.exception.ServerErrorCode;
import academy.tochkavhoda.competition.exception.ServerException;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

final class ServiceSupport {
    private ServiceSupport() { }

    static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    static Set<String> normalizeDirections(List<String> directions) throws ServerException {
        if (directions == null || directions.isEmpty()) {
            throw new ServerException(ServerErrorCode.WRONG_DIRECTIONS);
        }
        Set<String> result = new LinkedHashSet<>();
        for (String direction : directions) {
            if (isBlank(direction)) {
                throw new ServerException(ServerErrorCode.WRONG_DIRECTIONS);
            }
            result.add(direction.trim().toLowerCase(Locale.ROOT));
        }
        return result;
    }

    static User getUserByToken(UserDao userDao, String token) throws ServerException {
        if (isBlank(token)) {
            throw new ServerException(ServerErrorCode.WRONG_TOKEN);
        }
        User user = userDao.getByToken(token);
        if (user == null) {
            throw new ServerException(ServerErrorCode.WRONG_TOKEN);
        }
        return user;
    }

    static Participant getParticipantByToken(UserDao userDao, String token) throws ServerException {
        User user = getUserByToken(userDao, token);
        if (!(user instanceof Participant participant)) {
            throw new ServerException(ServerErrorCode.NOT_PARTICIPANT);
        }
        return participant;
    }

    static Expert getExpertByToken(UserDao userDao, String token) throws ServerException {
        User user = getUserByToken(userDao, token);
        if (!(user instanceof Expert expert)) {
            throw new ServerException(ServerErrorCode.NOT_EXPERT);
        }
        return expert;
    }
}
