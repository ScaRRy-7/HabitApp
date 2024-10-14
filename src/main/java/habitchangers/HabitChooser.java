package habitchangers;

import entities.Habit;
import entities.User;
import in.Reader;
import out.HabitChooserWriter;
import validate.HabitChooserValidator;
import wait.Waiter;

/**
 * Отвечает за выбор пользователем одной из его существующих привычек.
 * Класс использует {@link HabitIndicator} для отображения списка привычек пользователя,
 * {@link HabitChooserWriter} для вывода запроса на выбор привычки,
 * {@link Reader} для чтения ввода пользователя,
 * {@link HabitChooserValidator} для проверки корректности выбранной привычки,
 * и {@link Waiter} для временной задержки.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class HabitChooser {

    private final HabitIndicator habitIndicator;
    private final HabitChooserWriter writer;
    private final Reader reader;
    private final HabitChooserValidator validator;
    private final Waiter waiter;

    /**
     * Конструктор для создания объекта HabitChooser с заданными зависимостями.
     *
     * @param habitIndicator объект для отображения списка привычек
     * @param writer         объект для вывода запроса на выбор привычки
     * @param reader         объект для чтения ввода пользователя
     * @param validator      объект для проверки корректности выбранной привычки
     * @param waiter         объект для реализации временной задержки
     */
    public HabitChooser(HabitIndicator habitIndicator, HabitChooserWriter writer, Reader reader,
                        HabitChooserValidator validator, Waiter waiter) {
        this.habitIndicator = habitIndicator;
        this.writer = writer;
        this.reader = reader;
        this.validator = validator;
        this.waiter = waiter;
    }

    /**
     * Конструктор для создания объекта HabitChooser с использованием параметров по умолчанию.
     */
    public HabitChooser() {
        this.habitIndicator = new HabitIndicator();
        this.writer = new HabitChooserWriter();
        this.reader = new Reader();
        this.validator = new HabitChooserValidator();
        this.waiter = new Waiter();
    }

    /**
     * Позволяет пользователю выбрать одну из его существующих привычек.
     * Сначала отображается список привычек пользователя, затем пользователю предлагается ввести номер выбранной привычки.
     * Если пользователь вводит корректный номер, возвращается соответствующая привычка.
     * Если пользователь вводит некорректный номер, ему предлагается выбрать привычку еще раз.
     *
     * @param user пользователь, привычки которого доступны для выбора
     * @return выбранная пользователем привычка
     */
    public Habit chooseHabit(User user) {
        Habit choosedHabit = null;
        habitIndicator.showHabits(user);
        writer.askHabitNumber();
        String habitNumber = reader.read();

        if (validator.isValidHabitNumber(user, habitNumber)) {
            int habitIndex = Integer.parseInt(habitNumber) - 1;
            choosedHabit = user.getHabits().get(habitIndex);
        } else {
            writer.reportIncorrectHabitNumber();
            waiter.waitSecond();
            choosedHabit = chooseHabit(user);
        }
        return choosedHabit;
    }
}
