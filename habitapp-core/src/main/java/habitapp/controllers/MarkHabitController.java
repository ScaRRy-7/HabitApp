package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.MessageDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления пометками привычек пользователей.
 * Предоставляет API для пометки привычек как выполненных.
 */
@Tag(name = "Habit Management", description = "API for marking habits")
@RestController
@RequestMapping("/habitapp")
@Loggable
public class MarkHabitController {

    private final HabitsServiceImpl habitsServiceImpl;
    private final ObjectMapper objectMapper;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param habitsServiceImpl Сервис для работы с привычками.
     * @param objectMapper      Объект для преобразования объектов в JSON.
     */
    public MarkHabitController(HabitsServiceImpl habitsServiceImpl, ObjectMapper objectMapper) {
        this.habitsServiceImpl = habitsServiceImpl;
        this.objectMapper = objectMapper;
    }

    /**
     * Помечает привычку пользователя как выполненную.
     *
     * @param authHeader Заголовок авторизации.
     * @param habitDTO  Объект с данными привычки для пометки.
     * @return Ответ с сообщением о результате операции.
     * @throws JsonProcessingException В случае ошибки при обработке JSON.
     */
    @Operation(
            summary = "Mark a habit",
            description = "Marks a user's habit as completed"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Habit successfully marked",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Habit marked successfully\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid JSON format",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Incorrect json format\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Habit not found",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Habit not found\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User  is not authorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"User  is not authorized\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Habit cannot be marked because it is already marked",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Habit cannot be marked because it already marked\"}")
                    )
            )
    })
    @PutMapping("/markhabit")
    public ResponseEntity<String> markHabit(
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Habit details", required = true, schema = @Schema(implementation = HabitDTO.class))
            @RequestBody HabitDTO habitDTO) throws JsonProcessingException {

        MessageDTO messageDTO;
        try {
            if (habitDTO == null) {
                messageDTO = new MessageDTO("{\"message\": \"Habit cannot be null\"}");
                String jsonResponse = objectMapper.writeValueAsString(messageDTO);
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                        .body(jsonResponse);
            }

            habitsServiceImpl.markHabit(authHeader, habitDTO);
            messageDTO = new MessageDTO("{\"message\": \"Habit marked successfully\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (HttpMessageNotReadableException e) {
            messageDTO = new MessageDTO("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        }
    }
}