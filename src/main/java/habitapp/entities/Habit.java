package habitapp.entities;

import habitapp.enums.HabitFrequency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

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
    private HabitFrequency frequenсy;

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
     * Список дат, когда привычка была выполнена.
     */
    @Getter
    private final List<LocalDateTime> daysHabitCompleted = new ArrayList<>();

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
        this.frequenсy = frequency;
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
        this.frequenсy = frequency;
        this.createdDateTime = createdDateTime;
        this.isCompleted = isCompleted;
    }

    /**
     * Создает новую пустую привычку.
     */
    public Habit() {}

    /**
     * Возвращает частоту привычки.
     *
     * @return частота привычки
     */
    public HabitFrequency getFrequency() {
        return frequenсy;
    }

    /**
     * Возвращает статус привычки (выполнена или нет).
     *
     * @return true, если привычка выполнена, иначе false
     */
    public boolean isCompleted() {
        return isCompleted;
    }

    /**
     * Устанавливает статус привычки как "выполнена".
     */
    public void setcompleted() {
        isCompleted = true;
    }

    /**
     * Устанавливает статус привычки как "не выполнена".
     */
    public void setUncompleted() {
        isCompleted = false;
    }

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