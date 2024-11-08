package habitapp.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO (Data Transfer Object) для передачи сообщений.
 * Содержит одно поле - сообщение.
 */
@AllArgsConstructor
@Getter
@Setter
public class MessageDTO {

    /**
     * Сообщение.
     */
    private String message;
}