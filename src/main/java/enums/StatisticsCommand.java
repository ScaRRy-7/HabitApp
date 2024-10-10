package enums;

public enum StatisticsCommand {
    STATISTICS_FOR_PERIOD("Статистика выполнения привычки за указанный период (день/неделя/месяц"),
    CALCULATE_STREAKS("Подсчет текущих серий выполнения привычек"),
    CALCULATE_SUCCESS_COMPLETION_FOR_PERIOD("Процент успешного выполнения привычек за определенный период"),
    GENERATE_COMPLETION_REPORT("Формирование отчета для пользователя по прогрессу выполнения"),
    RETURN_TO_MENU("Вернуться в меню");

    private final String name;

    StatisticsCommand(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
