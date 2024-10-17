package habitchangers;

import entities.Habit;
import entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import out.HabitChooserWriter;
import validate.HabitChooserValidator;
import wait.Waiter;
import in.Reader;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HabitChooserTest {

    private HabitChooser habitChooser;
    private HabitIndicator habitIndicator;
    private HabitChooserWriter writer;
    private Reader reader;
    private HabitChooserValidator validator;
    private Waiter waiter;

    @BeforeEach
    public void setUp() {
        habitIndicator = mock(HabitIndicator.class);
        writer = mock(HabitChooserWriter.class);
        reader = mock(Reader.class);
        validator = mock(HabitChooserValidator.class);
        waiter = mock(Waiter.class);

        habitChooser = new HabitChooser(habitIndicator, writer, reader, validator, waiter);
    }

    @Test
    public void testChooseHabit_ValidInput() {
        User user = mock(User.class);
        Habit habit = mock(Habit.class);
        when(user.getHabits()).thenReturn(List.of(habit));
        when(reader.read()).thenReturn("1");
        when(validator.isValidHabitNumber(user, "1")).thenReturn(true);

        Habit chosenHabit = habitChooser.chooseHabit(user);

        assertThat(chosenHabit).isEqualTo(habit);
        verify(habitIndicator).showHabits(user);
        verify(writer).askHabitNumber();
        verifyNoInteractions(waiter);
    }

    @Test
    public void testChooseHabit_InvalidInputThenValid() {
        User user = mock(User.class);
        Habit habit = mock(Habit.class);
        when(user.getHabits()).thenReturn(List.of(habit));

        // Указываем последовательность возвращаемых значений
        when(reader.read()).thenReturn("0", "1");
        when(validator.isValidHabitNumber(user, "0")).thenReturn(false);
        when(validator.isValidHabitNumber(user, "1")).thenReturn(true);

        Habit chosenHabit = habitChooser.chooseHabit(user);

        assertThat(chosenHabit).isEqualTo(habit);
        verify(writer, times(2)).askHabitNumber();
        verify(writer).reportIncorrectHabitNumber();
        verify(waiter).waitSecond();
    }
}