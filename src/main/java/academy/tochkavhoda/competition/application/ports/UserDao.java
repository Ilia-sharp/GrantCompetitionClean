package academy.tochkavhoda.competition.application.ports;

import academy.tochkavhoda.competition.exception.ServerException;
import academy.tochkavhoda.competition.domain.models.Participant;
import academy.tochkavhoda.competition.domain.models.User;

import java.util.List;

public interface UserDao {
    void insert(User user) throws ServerException;

    User getByLogin(String login);

    User getByToken(String token);

    String login(User user);

    void logout(String token);

    void delete(User user);

    List<Participant> getAllParticipants();
}
