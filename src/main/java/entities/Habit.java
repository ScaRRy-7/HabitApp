package entities;

import enums.HabitFrequency;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Класс Habit представляет объект привычки пользователя.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class Habit implements Comparable<Habit> {
    /**
     * Название привычки.
     */
    private String name;

    /**
     * Описание привычки.
     */
    private String description;

    /**
     * Частота привычки.
     */
    private HabitFrequency frequenсy;

    /**
     * Дата и время создания привычки.
     */
    private LocalDateTime createdDateTime;

    /**
     * Форматтер для даты и времени.
     */
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Флаг, показывающий, была ли привычка выполнена.
     */
    private boolean isComplited;

    /**
     * Список дат, когда привычка была выполнена.
     */
    private final List<LocalDateTime> daysHabitComplited = new ArrayList<>();

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
        this.isComplited = false;
    }

    /**
     * Возвращает название привычки.
     *
     * @return название привычки
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает описание привычки.
     *
     * @return описание привычки
     */
    public String getDescription() {
        return description;
    }

    /**
     * Возвращает частоту привычки.
     *
     * @return частота привычки
     */
    public HabitFrequency getFrequency() {
        return frequenсy;
    }

    /**
     * Возвращает дату и время создания привычки.
     *
     * @return дата и время создания привычки
     */
    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    /**
     * Возвращает список дат, когда привычка была выполнена.
     *
     * @return список дат выполнения привычки
     */
    public List<LocalDateTime> getDaysHabitComplited() {
        return daysHabitComplited;
    }

    /**
     * Возвращает статус привычки (выполнена или нет).
     *
     * @return true, если привычка выполнена, иначе false
     */
    public boolean isComplited() {
        return isComplited;
    }

    /**
     * Устанавливает статус привычки как "выполнена".
     */
    public void setComplited() {
        isComplited = true;
    }

    /**
     * Устанавливает статус привычки как "не выполнена".
     */
    public void setUncomplited() {
        isComplited = false;
    }

    /**
     * Устанавливает новое название привычки.
     *
     * @param name новое название привычки
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Устанавливает новое описание привычки.
     *
     * @param description новое описание привычки
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Устанавливает новую частоту привычки.
     *
     * @param frequenсy новая частота привычки
     */
    public void setFrequenсy(HabitFrequency frequenсy) {
        this.frequenсy = frequenсy;
    }

    /**
     * Возвращает строковое представление объекта Habit.
     *
     * @return строковое представление объекта Habit
     *
     */
    @Override
    public String toString() {
        return "Название привычки: " + getName() + "\n\tОписание привычки: " + getDescription() +
                "\n\tЧастота: " + getFrequency().getName() + "\n\tСоздана: " +
                getCreatedDateTime().format(formatter) + "\n\tСтатус: " + (isComplited ? "Выполнена" : "Не выполнена");
    }

    /**
     * Сравнивает текущий объект Habit с другим объектом Habit.
     *
     * @param anotherHabit другой объект Habit для сравнения
     * @return отрицательное число, если текущий объект меньше другого, 0, если они равны, положительное число, если текущий объект больше другого
     */
    @Override
    public int compareTo(Habit anotherHabit) {
        return this.createdDateTime.compareTo(anotherHabit.getCreatedDateTime());
    }
}

