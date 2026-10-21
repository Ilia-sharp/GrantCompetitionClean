package academy.tochkavhoda.competition.application.ports;

import academy.tochkavhoda.competition.exception.ServerException;
import academy.tochkavhoda.competition.domain.models.CompetitionResult;
import academy.tochkavhoda.competition.domain.models.GrantApplication;

import java.util.List;
import java.util.Map;

public interface CompetitionDao {
    Map<GrantApplication, List<Integer>> getAllApplicationsWithRatings();

    boolean isFinished();

    void saveResult(CompetitionResult result) throws ServerException;

    CompetitionResult getResult();
}
