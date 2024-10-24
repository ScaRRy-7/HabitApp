package habitapp.services.statistics;

import habitapp.entities.Habit;
import habitapp.entities.User;

public interface StatisticsCreator {
    void getStatistics(User user, Habit habit);
}
