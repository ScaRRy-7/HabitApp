package entrypoint;

/**
 * Перечисление `Commands` представляет доступные команды в приложении.
 *
 * @author ScaRRy-7
 * @version 1.0
 * @since 1.0
 */
public enum Commands {
    /**
     * Команда "Изменить профиль".
     */
    CHANGEPROFILE(1),
    /**
     * Команда "Редактировать привычки".
     */
    EDITHABITS(2),
    /**
     * Команда "Статистика привычек".
     */
    HABITSTATS(3),
    /**
     * Команда "Выйти".
     */
    EXIT(4);

    /**
     * Числовое значение, связанное с командой.
     */
    private final int number;

    /**
     * Создает новый экземпляр `Commands` с указанным числовым значением.
     *
     * @param number числовое значение, связанное с командой
     */
    private Commands(int number) {
        this.number = number;
    }
}