package validate;

import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CommandHabitValidatorTest {

    private CommandHabitValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new CommandHabitValidator();
    }

    @Test
    public void testIsValidCommand() {
        assertTrue(validator.isValidCommand("1"));
        assertTrue(validator.isValidCommand("6"));
        assertFalse(validator.isValidCommand("7"));
        assertFalse(validator.isValidCommand("0"));
    }

    @Test
    public void testIsValidHabitName() {
        assertTrue(validator.isValidHabitName("Exercise"));
        assertTrue(validator.isValidHabitName("Привычка123"));
        assertFalse(validator.isValidHabitName("Ex"));
        assertFalse(validator.isValidHabitName("This habit name is way too long to be valid"));
    }

    @Test
    public void testIsValidDescription() {
        assertTrue(validator.isValidDescription("Daily exercise routine"));
        assertTrue(validator.isValidDescription("Описание123"));
        assertFalse(validator.isValidDescription("Ex"));
        assertFalse(validator.isValidDescription("This description is way too long to be considered valid because it exceeds the maximum allowed length of one hundred characters"));
    }

    @Test
    public void testIsValidFrequency() {
        assertTrue(validator.isValidFrequency("1"));
        assertTrue(validator.isValidFrequency("2"));
        assertFalse(validator.isValidFrequency("3"));
        assertFalse(validator.isValidFrequency("0"));
    }

}