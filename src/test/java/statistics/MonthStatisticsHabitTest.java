package statistics;

import adminstration.AdminBlocator;
import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import habitchangers.HabitUnmarker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import out.MonthStatisticsHabitWriter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class MonthStatisticsHabitTest {

    @Mock
    private HabitUnmarker habitUnmarker;

    @Mock
    private MonthStatisticsHabitWriter writer;

    @InjectMocks
    private MonthStatisticsHabit monthStatisticsHabit;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getStatistics_shouldCallHabitUnmarkerAndWriterMethods_whenUserHasHabit() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "password123");
        Habit habit = new Habit("Read Book", "Read for 30 minutes", HabitFrequency.DAILY);

        // Act
        monthStatisticsHabit.getStatistics(user, habit);

        // Assert
        verify(habitUnmarker, times(1)).checkHabits(user);
        verify(writer, times(1)).writeWeekStatistics(anyList());
    }

    @Test
    void getStatistics_shouldCallHabitUnmarkerAndWriterMethods_whenUserHasWeeklyHabit() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "password123");
        Habit habit = new Habit("Exercise", "Do 30 minutes of exercise", HabitFrequency.DAILY);


        // Act
        monthStatisticsHabit.getStatistics(user, habit);

        // Assert
        verify(habitUnmarker, times(1)).checkHabits(user);
        verify(writer, times(1)).writeWeekStatistics(anyList());
    }

    @Test
    void getStatistics_shouldCallHabitUnmarkerAndWriterMethods_whenUserHasNoHabit() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "password123");
        Habit habit = new Habit("Read Book", "Read for 30 minutes", HabitFrequency.DAILY);

        // Act
        monthStatisticsHabit.getStatistics(user, habit);

        // Assert
        verify(habitUnmarker, times(1)).checkHabits(user);
        verify(writer, times(1)).writeWeekStatistics(anyList());
    }
}
