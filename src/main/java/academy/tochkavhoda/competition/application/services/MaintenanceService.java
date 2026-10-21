package academy.tochkavhoda.competition.application.services;

import academy.tochkavhoda.competition.application.ports.DebugDao;

public final class MaintenanceService {
    private final DebugDao debugDao;

    public MaintenanceService(DebugDao debugDao) {
        this.debugDao = debugDao;
    }

    public void clearDatabase() {
        debugDao.clear();
    }
}
