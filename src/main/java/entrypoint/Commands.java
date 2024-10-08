package entrypoint;

public enum Commands {
    CHANGEPROFILE(1),
    EDITHABITS(2),
    HABITSTATS(3),
    EXIT(4);

    private final int number;

    private Commands(int number) {
        this.number = number;
    }
}
