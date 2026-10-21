package academy.tochkavhoda.competition.application.ports;

import academy.tochkavhoda.competition.exception.ServerException;
import academy.tochkavhoda.competition.domain.models.GrantApplication;
import academy.tochkavhoda.competition.domain.models.Participant;

import java.util.Collection;
import java.util.List;

public interface ApplicationDao {
    List<Integer> insert(List<GrantApplication> applications) throws ServerException;

    GrantApplication getById(int id);

    List<GrantApplication> getByParticipant(Participant participant);

    List<GrantApplication> getByDirections(Collection<String> directions);

    void delete(GrantApplication application) throws ServerException;
}
