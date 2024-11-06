package habitapp.repositories;

import java.time.LocalDateTime;
import java.util.List;

public interface CompletedDaysRepository {

    public List<LocalDateTime> getCompletedDays(int habitId);
    public void createCompletedDay(int habitId);
    public LocalDateTime getLastCompletedDay(int habitId);
}
