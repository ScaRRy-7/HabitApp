package enums;

public enum Sorting {
    DATE("По дате"),
    FREQUENCY("По частоте");

    private final String name;

    public String getName() {
        return name;
    }

    private Sorting(String name) {
        this.name = name;
    }
}
