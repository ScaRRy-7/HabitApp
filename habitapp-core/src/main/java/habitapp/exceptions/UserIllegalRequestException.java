package habitapp.exceptions;

import lombok.Getter;

/**
 * Исключение, возникающее при некорректном запросе пользователя.
 * <p>
 * Это исключение используется для обработки случаев, когда запрос пользователя
 * не соответствует необходимым условиям или правилам. Оно содержит код ошибки
 * и сообщение об ошибке.
 * </p>
 */
@Getter
public class UserIllegalRequestException extends RuntimeException {
    /**
     * Код ошибки, связанный с некорректным запросом.
     * -- GETTER --
     *  Получает код ошибки.
     *
     * @return Код ошибки, связанный с некорректным запросом.

     */
    private final int errorCode;

    /**
     * Конструктор для создания исключения с указанным кодом ошибки и сообщением.
     *
     * @param errorCode Код ошибки, который указывает на тип некорректного запроса.
     * @param message   Сообщение, описывающее ошибку.
     */
    public UserIllegalRequestException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

}
