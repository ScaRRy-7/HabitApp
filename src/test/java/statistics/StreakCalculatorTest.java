package statistics;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import habitchangers.HabitUnmarker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import out.StreakCalculatorWriter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

class StreakCalculatorTest {
    @Mock
    private StreakCalculatorWriter streakCalculatorWriterMock;

    @Mock
    private HabitUnmarker habitUnmarkerMock;

    @InjectMocks
    private StreakCalculator streakCalculator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void start_shouldCallHabitUnmarkerAndWriterMethods_whenUserHasHabits() {
        // Arrange
        User user = new User("John Doe", "john@example.com", "password123");
        Habit habit1 = new Habit("Read Book", "Read for 30 minutes", HabitFrequency.DAILY);
        Habit habit2 = new Habit("Exercise", "Do 30 minutes of workout", HabitFrequency.WEEKLY);
        user.addHabit(habit1);
        user.addHabit(habit2);

        // Act
        streakCalculator.start(user);

        // Assert
        verify(habitUnmarkerMock, times(1)).checkHabits(user);
        verify(streakCalculatorWriterMock, times(2)).write(any(), anyInt());
    }

    @Test
    void calculateStreak_shouldReturnCorrectStreak_forDailyHabit() {
        // Arrange
        Habit habit = new Habit("Read Book", "Read for 30 minutes", HabitFrequency.DAILY);

        habit.getDaysHabitComplited().add(LocalDateTime.now());
        habit.getDaysHabitComplited().add(LocalDateTime.now().minusDays(1));
        habit.getDaysHabitComplited().add(LocalDateTime.now().minusDays(2));

        // Act
        int streak = streakCalculator.calculateStreak(habit);

        // Assert
        assert streak == 3;
    }

    @Test
    void calculateStreak_shouldReturnCorrectStreak_forWeeklyHabit() {
        // Arrange
        Habit habit = new Habit("Exercise", "Do 30 minutes of workout", HabitFrequency.WEEKLY);

        habit.getDaysHabitComplited().add(LocalDateTime.now());
        habit.getDaysHabitComplited().add(LocalDateTime.now().minusDays(7));
        habit.getDaysHabitComplited().add(LocalDateTime.now().minusDays(14));

        // Act
        int streak = streakCalculator.calculateStreak(habit);

        // Assert
        assert streak == 3;
    }
}
