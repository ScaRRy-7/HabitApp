package habitapp.entities;

import habitapp.enums.HabitFrequency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Класс Habit представляет объект привычки пользователя.
 * <p>
 * Этот класс содержит информацию о привычке, такую как название, описание, частота,
 * дата создания и статус выполнения. Он также предоставляет методы для управления
 * статусом привычки и её представления.
 * </p>
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class Habit implements Comparable<Habit> {

    /**
     * Название привычки.
     */
    @Getter
    @Setter
    private String name;

    /**
     * Описание привычки.
     */
    @Getter
    @Setter
    private String description;

    /**
     * Частота выполнения привычки.
     */
    @Setter
    @Getter
    private HabitFrequency frequency;

    /**
     * Дата и время создания привычки.
     *
     * @return дата и время создания привычки
     */
    @Getter
    @Setter
    private LocalDateTime createdDateTime;

    /**
     * Форматтер для даты и времени.
     */
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Статус выполнения привычки.
     */
    @Getter
    @Setter
    private boolean isCompleted;

    /**
     * Создает новую привычку с заданными параметрами.
     *
     * @param name        название привычки
     * @param description описание привычки
     * @param frequency   частота привычки
     */
    public Habit(String name, String description, HabitFrequency frequency) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createdDateTime = LocalDateTime.now();
        this.isCompleted = false;
    }

    /**
     * Создает новую привычку с заданными параметрами, включая дату создания и статус выполнения.
     *
     * @param name            название привычки
     * @param description     описание привычки
     * @param frequency       частота привычки
     * @param createdDateTime дата и время создания привычки
     * @param isCompleted     статус выполнения привычки
     */
    public Habit(String name, String description, HabitFrequency frequency, LocalDateTime createdDateTime, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.createdDateTime = createdDateTime;
        this.isCompleted = isCompleted;
    }


    public Habit(String name, String description, HabitFrequency frequency, boolean isCompleted) {
        this.name = name;
        this.description = description;
        this.isCompleted = isCompleted;
        this.frequency = frequency;
    }

    public Habit() {}

    /**
     * Возвращает частоту привычки.
     *
     * @return частота привычки
     */

    /**
     * Возвращает строковое представление объекта Habit.
     *
     * @return строковое представление объекта Habit
     */
    @Override
    public String toString() {
        return "Название привычки: " + getName() + "\n\tОписание привычки: " + getDescription() +
                "\n\tЧастота: " + getFrequency().getName() + "\n\tСоздана: " +
                getCreatedDateTime().format(formatter) + "\n\tСтатус: " + (isCompleted ? "Выполнена" : "Не выполнена");
    }

    /**
     * Сравнивает текущий объект Habit с другим объектом Habit.
     *
     * @param anotherHabit другой объект Habit для сравнения
     * @return отрицательное число, если текущий объект меньше другого, 0, если они равны,
     * положительное число, если текущий объект больше другого
     */
    @Override
    public int compareTo(Habit anotherHabit) {
        return this.createdDateTime.compareTo(anotherHabit.getCreatedDateTime());
    }
}