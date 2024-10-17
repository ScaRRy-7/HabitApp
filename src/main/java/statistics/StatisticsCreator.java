package statistics;

import entities.Habit;
import entities.User;

public interface StatisticsCreator {
    void getStatistics(User user, Habit habit);
}
