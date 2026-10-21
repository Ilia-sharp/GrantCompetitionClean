package academy.tochkavhoda.competition.application.ports;

import academy.tochkavhoda.competition.exception.ServerException;
import academy.tochkavhoda.competition.domain.models.Expert;
import academy.tochkavhoda.competition.domain.models.GrantApplication;

import java.util.Map;

public interface RatingDao {
    Integer get(Expert expert, GrantApplication application);

    void set(Expert expert, GrantApplication application, int rating) throws ServerException;

    void delete(Expert expert, GrantApplication application) throws ServerException;

    Map<GrantApplication, Integer> getByExpert(Expert expert);
}
