package services.enums;

import lombok.Getter;

public enum HabitFrequency {
    DAILY("ежедневно"),
    WEEKLY("еженедельно");

    @Getter
    private final String name;

    HabitFrequency(String name) {
        this.name = name;
    }

    public static HabitFrequency getFrequencyByName(String name) {
        HabitFrequency returnFrequency = null;
        for (HabitFrequency frequency : HabitFrequency.values()) {
            if (frequency.getName().equals(name)) {
                returnFrequency = frequency;
            }
        }
        return returnFrequency;
    }
}
