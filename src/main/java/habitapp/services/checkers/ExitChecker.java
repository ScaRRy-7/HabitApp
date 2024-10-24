package habitapp.services.checkers;

import habitapp.services.entrypoint.Identification;

/**
 * Класс ExitChecker отвечает за проверку команды на выход из приложения.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class ExitChecker {
    /**
     * Объект класса Identification для запуска точки входа.
     */
    private static final Identification identification = new Identification();

    /**
     * Проверяет, является ли введенная строка командой на выход из приложения.
     * Если да, то запускает точку входа в приложение.
     *
     * @param str строка, которую нужно проверить
     */
    public static void check(String str) {
        if (str.equals("exit".toLowerCase())) {
            identification.start();
        }
    }
}
