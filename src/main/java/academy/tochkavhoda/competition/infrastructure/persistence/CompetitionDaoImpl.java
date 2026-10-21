package academy.tochkavhoda.competition.infrastructure.persistence;

import academy.tochkavhoda.competition.application.ports.CompetitionDao;
import academy.tochkavhoda.competition.domain.Database;
import academy.tochkavhoda.competition.exception.ServerException;
import academy.tochkavhoda.competition.domain.models.CompetitionResult;
import academy.tochkavhoda.competition.domain.models.GrantApplication;

import java.util.List;
import java.util.Map;

public class CompetitionDaoImpl implements CompetitionDao {
    private final Database database = Database.getInstance();

    @Override
    public Map<GrantApplication, List<Integer>> getAllApplicationsWithRatings() {
        return database.getAllApplicationsWithRatings();
    }

    @Override
    public boolean isFinished() {
        return database.isCompetitionFinished();
    }

    @Override
    public void saveResult(CompetitionResult result) throws ServerException {
        database.saveCompetitionResult(result);
    }

    @Override
    public CompetitionResult getResult() {
        return database.getCompetitionResult();
    }
}
