package validate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class IncomplitedHabitsMenuValidatorTest {

    private IncomplitedHabitsMenuValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new IncomplitedHabitsMenuValidator();
    }

    @Test
    public void testIsValidNumberOfIncHabit() {
        int numberOfIncHabits = 5;

        // Проверка допустимых значений
        assertTrue(validator.isValidNumberOfIncHabit("1", numberOfIncHabits));
        assertTrue(validator.isValidNumberOfIncHabit("5", numberOfIncHabits));

        // Проверка недопустимых значений
        assertFalse(validator.isValidNumberOfIncHabit("0", numberOfIncHabits)); // Номер не может быть 0
        assertFalse(validator.isValidNumberOfIncHabit("6", numberOfIncHabits)); // Номер превышает количество привычек
        assertFalse(validator.isValidNumberOfIncHabit("-1", numberOfIncHabits)); // Отрицательное число
        assertFalse(validator.isValidNumberOfIncHabit("abc", numberOfIncHabits)); // Не числовое значение
        assertFalse(validator.isValidNumberOfIncHabit("", numberOfIncHabits)); // Пустая строка
    }
}