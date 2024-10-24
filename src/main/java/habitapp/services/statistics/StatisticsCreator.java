package services.statistics;

import services.entities.Habit;
import services.entities.User;

public interface StatisticsCreator {
    void getStatistics(User user, Habit habit);
}
