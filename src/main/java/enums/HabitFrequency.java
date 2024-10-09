package enums;

public enum HabitFrequency {
    DAILY("ежедневно"),
    WEEKLY("еженедельно");

    private final String name;

    HabitFrequency(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
