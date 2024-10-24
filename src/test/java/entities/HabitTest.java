package entities;

import habitapp.entities.Habit;
import habitapp.services.enums.HabitFrequency;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HabitTest {

    private Habit habit;

    @BeforeEach
    void setUp() {
        habit = new Habit("Exercise", "Daily morning exercise", HabitFrequency.DAILY);
    }

    @Test
    @DisplayName("Тест метода getCreatedDateTime()")
    void testGetCreatedDateTime() {
        assertNotNull(habit.getCreatedDateTime());
    }

    @Test
    @DisplayName("Тест метода isComplitedInitiallyFalse()")
    void testIsComplitedInitiallyFalse() {
        assertFalse(habit.isComplited());
    }

    @Test
    @DisplayName("Тест метода setComplited()")
    void testSetComplited() {
        habit.setComplited();
        assertTrue(habit.isComplited());
    }

    @Test
    @DisplayName("Тест метода setUncomplited()")
    void testSetUncomplited() {
        habit.setComplited(); // First set to completed
        habit.setUncomplited();
        assertFalse(habit.isComplited());
    }

    @Test
    @DisplayName("Тест метода getDaysHabitComplited()")
    void testGetDaysHabitComplited() {
        List<LocalDateTime> days = habit.getDaysHabitComplited();
        assertNotNull(days);
        assertTrue(days.isEmpty());
    }

    @Test
    @DisplayName("Тест метода compareTo()")
    void testCompareTo() {
        Habit anotherHabit = new Habit("Meditation", "Daily meditation", HabitFrequency.DAILY);
        assertTrue(habit.compareTo(anotherHabit) <= 0 || habit.compareTo(anotherHabit) >= 0);
    }


}
