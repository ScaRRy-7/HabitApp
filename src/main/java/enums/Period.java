package enums;

public enum Period {
    DAY("День"),
    WEEK("Неделя"),
    MONTH("Месяц");

    private final String name;

    Period(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
