package academy.tochkavhoda.competition.application.services;

import academy.tochkavhoda.competition.application.ports.ApplicationDao;
import academy.tochkavhoda.competition.application.ports.RatingDao;
import academy.tochkavhoda.competition.application.ports.UserDao;
import academy.tochkavhoda.competition.application.results.ApplicationResult;
import academy.tochkavhoda.competition.application.results.RatedApplicationResult;
import academy.tochkavhoda.competition.domain.models.Expert;
import academy.tochkavhoda.competition.domain.models.GrantApplication;
import academy.tochkavhoda.competition.exception.ServerErrorCode;
import academy.tochkavhoda.competition.exception.ServerException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static academy.tochkavhoda.competition.application.services.ServiceSupport.getExpertByToken;

public final class RatingService {
    private static final int MIN_RATING = 1;
    private static final int MAX_RATING = 5;

    private final UserDao userDao;
    private final ApplicationDao applicationDao;
    private final RatingDao ratingDao;

    public RatingService(UserDao userDao, ApplicationDao applicationDao, RatingDao ratingDao) {
        this.userDao = userDao;
        this.applicationDao = applicationDao;
        this.ratingDao = ratingDao;
    }

    public void rateApplication(String token, Integer applicationId, Integer rating) throws ServerException {
        Expert expert = getExpertByToken(userDao, token);
        validate(applicationId, rating);
        GrantApplication application = getApplicationForExpert(expert, applicationId);
        if (ratingDao.get(expert, application) != null) {
            throw new ServerException(ServerErrorCode.ALREADY_RATED, application.getId());
        }
        ratingDao.set(expert, application, rating);
    }

    public void changeRating(String token, Integer applicationId, Integer rating) throws ServerException {
        Expert expert = getExpertByToken(userDao, token);
        validate(applicationId, rating);
        GrantApplication application = getApplicationForExpert(expert, applicationId);
        if (ratingDao.get(expert, application) == null) {
            throw new ServerException(ServerErrorCode.RATING_NOT_FOUND, application.getId());
        }
        ratingDao.set(expert, application, rating);
    }

    public void deleteRating(String token, Integer applicationId) throws ServerException {
        Expert expert = getExpertByToken(userDao, token);
        if (applicationId == null) {
            throw new ServerException(ServerErrorCode.WRONG_APPLICATION_ID);
        }
        GrantApplication application = applicationDao.getById(applicationId);
        if (application == null || ratingDao.get(expert, application) == null) {
            throw new ServerException(ServerErrorCode.RATING_NOT_FOUND, applicationId);
        }
        ratingDao.delete(expert, application);
    }

    public List<RatedApplicationResult> getRatedApplications(String token) throws ServerException {
        Expert expert = getExpertByToken(userDao, token);
        List<RatedApplicationResult> result = new ArrayList<>();
        for (Map.Entry<GrantApplication, Integer> entry : ratingDao.getByExpert(expert).entrySet()) {
            result.add(new RatedApplicationResult(ApplicationResult.from(entry.getKey()), entry.getValue()));
        }
        return result;
    }

    private GrantApplication getApplicationForExpert(Expert expert, int applicationId) throws ServerException {
        GrantApplication application = applicationDao.getById(applicationId);
        if (application == null) {
            throw new ServerException(ServerErrorCode.APPLICATION_NOT_FOUND, applicationId);
        }
        if (Collections.disjoint(expert.getDirections(), application.getDirections())) {
            throw new ServerException(ServerErrorCode.APPLICATION_OUT_OF_EXPERTISE, applicationId);
        }
        return application;
    }

    private static void validate(Integer applicationId, Integer rating) throws ServerException {
        if (applicationId == null) {
            throw new ServerException(ServerErrorCode.WRONG_APPLICATION_ID);
        }
        if (rating == null || rating < MIN_RATING || rating > MAX_RATING) {
            throw new ServerException(ServerErrorCode.WRONG_RATING);
        }
    }
}
