package habitapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import habitapp.enums.HabitFrequency;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO (Data Transfer Object) для привычки.
 * <p>
 * Этот класс используется для передачи данных о привычке между слоями приложения.
 * Он содержит основные атрибуты, такие как имя, описание и частота.
 * </p>
 */
@Getter
@Setter
public class HabitDTO {

    /**
     * Название привычки.
     */
    private String name;

    /**
     * Описание привычки.
     */
    private String description;

    /**
     * Частота выполнения привычки.
     */
    private HabitFrequency frequency;

    /**
     * Признак завершенности привычки.
     * По умолчанию - false.
     */
    @JsonProperty(defaultValue = "false")
    private boolean completed;

    /**
     * Дата и время создания привычки.
     * Формат: "yyyy-MM-dd HH:mm:ss".
     * По умолчанию - текущее время.
     */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdDateTime = LocalDateTime.now();
}