package entities;

import enums.HabitFrequency;
import lombok.Getter;
import lombok.Setter;

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

    @Getter
    @Setter
    private String name;


    @Getter
    @Setter
    private String description;


    @Setter
    private HabitFrequency frequenсy;


    /**
     * -- GETTER --
     *  Возвращает дату и время создания привычки.
     *
     * @return дата и время создания привычки
     */
    @Getter
    private LocalDateTime createdDateTime;


    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


    private boolean isComplited;

    @Getter
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

    public Habit(String name, String description, HabitFrequency frequency, LocalDateTime createdDateTime, boolean isComplited) {
        this.name = name;
        this.description = description;
        this.frequenсy = frequency;
        this.createdDateTime = createdDateTime;
        this.isComplited = isComplited;
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

