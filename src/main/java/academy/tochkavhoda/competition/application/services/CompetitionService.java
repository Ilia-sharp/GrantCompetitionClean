package academy.tochkavhoda.competition.application.services;

import academy.tochkavhoda.competition.application.ports.CompetitionDao;
import academy.tochkavhoda.competition.application.ports.UserDao;
import academy.tochkavhoda.competition.application.results.CompetitionResultResult;
import academy.tochkavhoda.competition.application.results.GrantResult;
import academy.tochkavhoda.competition.domain.models.CompetitionResult;
import academy.tochkavhoda.competition.domain.models.Grant;
import academy.tochkavhoda.competition.domain.models.GrantApplication;
import academy.tochkavhoda.competition.exception.ServerErrorCode;
import academy.tochkavhoda.competition.exception.ServerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static academy.tochkavhoda.competition.application.services.ServiceSupport.getUserByToken;

public final class CompetitionService {
    private final UserDao userDao;
    private final CompetitionDao competitionDao;
    private final Random random = new Random();

    public CompetitionService(UserDao userDao, CompetitionDao competitionDao) {
        this.userDao = userDao;
        this.competitionDao = competitionDao;
    }

    public CompetitionResultResult summarize(String token, Long fund, Double threshold) throws ServerException {
        getUserByToken(userDao, token);
        if (fund == null || fund <= 0) {
            throw new ServerException(ServerErrorCode.WRONG_FUND);
        }
        if (threshold == null || threshold.isNaN() || threshold < 0 || threshold > 5) {
            throw new ServerException(ServerErrorCode.WRONG_THRESHOLD);
        }
        if (competitionDao.isFinished()) {
            throw new ServerException(ServerErrorCode.COMPETITION_FINISHED);
        }

        List<RankedApplication> ranking = rank(competitionDao.getAllApplicationsWithRatings());
        long remaining = fund;
        List<Grant> grants = new ArrayList<>();
        for (RankedApplication ranked : ranking) {
            if (ranked.averageRating <= threshold) {
                break;
            }
            long sum = ranked.application.getRequestedSum();
            if (sum <= remaining) {
                grants.add(new Grant(ranked.application, ranked.averageRating, sum));
                remaining -= sum;
            }
        }
        CompetitionResult result = new CompetitionResult(fund, threshold, grants);
        competitionDao.saveResult(result);
        return toResult(result);
    }

    public CompetitionResultResult getResults(String token) throws ServerException {
        getUserByToken(userDao, token);
        CompetitionResult result = competitionDao.getResult();
        if (result == null) {
            throw new ServerException(ServerErrorCode.COMPETITION_NOT_FINISHED);
        }
        return toResult(result);
    }

    private List<RankedApplication> rank(Map<GrantApplication, List<Integer>> allRatings) {
        List<RankedApplication> ranking = new ArrayList<>();
        for (Map.Entry<GrantApplication, List<Integer>> entry : allRatings.entrySet()) {
            ranking.add(new RankedApplication(entry.getKey(), average(entry.getValue())));
        }
        Collections.shuffle(ranking, random);
        ranking.sort(Comparator.comparingDouble((RankedApplication item) -> item.averageRating).reversed()
                .thenComparingLong(item -> item.application.getRequestedSum()));
        return ranking;
    }

    private static double average(List<Integer> ratings) {
        if (ratings.isEmpty()) {
            return 0;
        }
        long total = 0;
        for (int rating : ratings) {
            total += rating;
        }
        return (double) total / ratings.size();
    }

    private static CompetitionResultResult toResult(CompetitionResult result) {
        List<GrantResult> grants = new ArrayList<>();
        long distributed = 0;
        for (Grant grant : result.getGrants()) {
            GrantApplication application = grant.getApplication();
            grants.add(new GrantResult(application.getId(), application.getTitle(),
                    application.getParticipant().getCompanyName(), grant.getAverageRating(), grant.getSum()));
            distributed += grant.getSum();
        }
        return new CompetitionResultResult(result.getFund(), result.getThreshold(), distributed,
                result.getFund() - distributed, grants);
    }

    private static final class RankedApplication {
        private final GrantApplication application;
        private final double averageRating;

        private RankedApplication(GrantApplication application, double averageRating) {
            this.application = application;
            this.averageRating = averageRating;
        }
    }
}
