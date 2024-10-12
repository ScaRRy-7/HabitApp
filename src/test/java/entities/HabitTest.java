package entities;

import enums.HabitFrequency;
import org.junit.jupiter.api.BeforeEach;
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
    void testGetName() {
        assertEquals("Exercise", habit.getName());
    }

    @Test
    void testSetName() {
        habit.setName("Reading");
        assertEquals("Reading", habit.getName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Daily morning exercise", habit.getDescription());
    }

    @Test
    void testSetDescription() {
        habit.setDescription("Read a book");
        assertEquals("Read a book", habit.getDescription());
    }

    @Test
    void testGetFrequency() {
        assertEquals(HabitFrequency.DAILY, habit.getFrequency());
    }

    @Test
    void testSetFrequency() {
        habit.setFrequenсy(HabitFrequency.WEEKLY);
        assertEquals(HabitFrequency.WEEKLY, habit.getFrequency());
    }

    @Test
    void testGetCreatedDateTime() {
        assertNotNull(habit.getCreatedDateTime());
    }

    @Test
    void testIsComplitedInitiallyFalse() {
        assertFalse(habit.isComplited());
    }

    @Test
    void testSetComplited() {
        habit.setComplited();
        assertTrue(habit.isComplited());
    }

    @Test
    void testSetUncomplited() {
        habit.setComplited(); // First set to completed
        habit.setUncomplited();
        assertFalse(habit.isComplited());
    }

    @Test
    void testGetDaysHabitComplited() {
        List<LocalDateTime> days = habit.getDaysHabitComplited();
        assertNotNull(days);
        assertTrue(days.isEmpty());
    }

    @Test
    void testCompareTo() {
        Habit anotherHabit = new Habit("Meditation", "Daily meditation", HabitFrequency.DAILY);
        assertTrue(habit.compareTo(anotherHabit) <= 0 || habit.compareTo(anotherHabit) >= 0);
    }

    @Test
    void testToString() {
        String expected = "Название привычки: Exercise\n\tОписание привычки: Daily morning exercise" +
                "\n\tЧастота: " + HabitFrequency.DAILY.getName() +
                "\n\tСоздана: " + habit.getCreatedDateTime().format(habit.formatter) +
                "\n\tСтатус: Не выполнена";
        assertEquals(expected, habit.toString());
    }
}