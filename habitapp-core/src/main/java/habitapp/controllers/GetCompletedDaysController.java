package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.MessageDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Контроллер для получения статистики привычек.
 * Предоставляет API для получения информации о выполненных днях для заданной привычки.
 */
@Tag(name = "Statistics", description = "API для получения статистики привычек")
@RestController
@RequestMapping("/habitapp")
@Loggable
public class GetCompletedDaysController {

    private final HabitsServiceImpl habitsServiceImpl;
    private final ObjectMapper objectMapper;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param habitsServiceImpl Сервис для работы с привычками.
     * @param objectMapper      Объект для преобразования объектов в JSON.
     */
    public GetCompletedDaysController(HabitsServiceImpl habitsServiceImpl, ObjectMapper objectMapper) {
        this.habitsServiceImpl = habitsServiceImpl;
        this.objectMapper = objectMapper;

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Получает статистику по заданной привычке.
     *
     * @param authHeader Заголовок авторизации.
     * @param habitDTO  Объект с данными привычки для получения статистики.
     * @return Ответ с информацией о выполненных днях для заданной привычки.
     * @throws JsonProcessingException В случае ошибки при обработке JSON.
     */
    @Operation(
            summary = "Get habit statistics",
            description = "Get statistics about requested habit"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Habit's statistic successfully retrieved",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = LocalDateTime.class))
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User  is not authorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid JSON",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @PostMapping("/statistics")
    public ResponseEntity<String> getHabitStatistics(@RequestHeader("Authorization") String authHeader, @RequestBody HabitDTO habitDTO) throws JsonProcessingException {
        MessageDTO messageDTO;

        try {
            List<LocalDateTime> habitDTOList = habitsServiceImpl.getCompletedDays(authHeader, habitDTO);
            messageDTO = new MessageDTO(habitDTOList.toString());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            messageDTO = new MessageDTO("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        }
    }
}