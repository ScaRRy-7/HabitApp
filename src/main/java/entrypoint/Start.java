package entrypoint;

import org.slf4j.*;

public final class Start {

    private static final Logger logger = LoggerFactory.getLogger(Start.class);

    public static void main(String[] args) {
        /* программа начинается с попытки идентификации пользователя

        если почта не будет найдена - редирект в регистрацию
        если почта найдена - редирект в авторизацию

         */
        Identification identification = new Identification();
        logger.info("создан обьект идентификации из метода main");
        identification.start();
    }
}
