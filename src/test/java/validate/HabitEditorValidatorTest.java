package validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HabitEditorValidatorTest {

    private HabitEditorValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new HabitEditorValidator();
    }

    @Test
    public void testIsValidNumberForRedacting() {
        assertTrue(validator.isValidNumberForRedacting("1"));
        assertTrue(validator.isValidNumberForRedacting("2"));
        assertTrue(validator.isValidNumberForRedacting("3"));
        assertFalse(validator.isValidNumberForRedacting("4"));
        assertFalse(validator.isValidNumberForRedacting("0"));
        assertFalse(validator.isValidNumberForRedacting("abc"));
    }

    @Test
    public void testIsValidHabitName() {
        assertTrue(validator.isValidHabitName("Exercise"));
        assertTrue(validator.isValidHabitName("Привычка123"));
        assertFalse(validator.isValidHabitName("Ex"));
        assertFalse(validator.isValidHabitName("ThisHabitNameIsWayTooLongToBeValid"));
        assertFalse(validator.isValidHabitName("Invalid Name!")); // Пробелы и спецсимволы недопустимы
    }

    @Test
    public void testIsValidHabitDescription() {
        assertTrue(validator.isValidHabitDescription("Daily exercise routine"));
        assertTrue(validator.isValidHabitDescription("Описание123"));
        assertFalse(validator.isValidHabitDescription("Ex"));
        assertFalse(validator.isValidHabitDescription("This description is way too long to be considered valid because it exceeds the maximum allowed length of one hundred characters"));
        assertFalse(validator.isValidHabitDescription("Invalid description!")); // Спецсимволы, кроме запятой и точки, недопустимы
    }

    @Test
    public void testIsValidHabitFrequencyNumber() {
        assertTrue(validator.isValidHabitFrequencyNumber("1"));
        assertTrue(validator.isValidHabitFrequencyNumber("2"));
        assertFalse(validator.isValidHabitFrequencyNumber("3"));
        assertFalse(validator.isValidHabitFrequencyNumber("0"));
        assertFalse(validator.isValidHabitFrequencyNumber("abc"));
    }
}