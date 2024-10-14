package entrypoint;

import org.slf4j.*;

/**
 * Точка входа в приложение.
 * Этот класс запускает процесс идентификации пользователя, который направляет его на регистрацию или аутентификацию.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class Start {

    private static final Logger logger = LoggerFactory.getLogger(Start.class);

    /**
     * Точка входа в приложение.
     * Создает объект идентификации и запускает процесс идентификации пользователя.
     *
     * @param args аргументы командной строки (не используются)
     */
    public static void main(String[] args) {

        Identification identification = new Identification();
        logger.info("создан обьект идентификации из метода main");
        identification.start();
    }
}
