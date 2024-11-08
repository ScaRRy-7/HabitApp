package habitapp.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * Перечисление для частоты привычек.
 * <p>
 * Это перечисление содержит значения, которые определяют частоту  привычки,
 * такую как "ежедневно" и "еженедельно". Оно также предоставляет метод для
 * получения частоты привычки по ее имени.
 * </p>
 */
@Getter
public enum HabitFrequency {
    DAILY("ежедневно"),
    WEEKLY("еженедельно");

    /**
     * Название частоты привычки.
     */
    @JsonValue
    private final String name;

    /**
     * Конструктор для создания экземпляра частоты привычки.
     *
     * @param name Название частоты привычки.
     */
    HabitFrequency(String name) {
        this.name = name;
    }

    /**
     * Получает частоту привычки по ее имени.
     *
     * @param name Название частоты привычки.
     * @return Экземпляр {@link HabitFrequency}, соответствующий переданному имени.
     * @throws IllegalArgumentException если не существует частоты привычки с указанным именем.
     */

    public static HabitFrequency getFrequencyByName(String name) {
        for (HabitFrequency frequency : HabitFrequency.values()) {
            if (frequency.getName().equals(name)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("this habit frequency does not exist!: " + name);
    }

}
