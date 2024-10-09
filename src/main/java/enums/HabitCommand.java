package enums;

public enum HabitCommand {
    CREATEHABIT("Создание привычки"),
    REDACTHABIT("Редактирование привычки"),
    DELETEHABIT("Удаление привычки"),
    SHOWHMYHABITS("Показать мои привычки"),
    RETURNTOMENU("Вернуться в меню");

    private final String name;


    HabitCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

