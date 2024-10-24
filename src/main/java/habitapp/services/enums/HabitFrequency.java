package habitapp.services.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum HabitFrequency {
    DAILY("ежедневно"),
    WEEKLY("еженедельно");

    @JsonValue
    private final String name;

    HabitFrequency(String name) {
        this.name = name;
    }

    public static HabitFrequency getFrequencyByName(String name) {
        for (HabitFrequency frequency : HabitFrequency.values()) {
            if (frequency.getName().equals(name)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("Нет такого частота привычки: " + name);
    }
}
