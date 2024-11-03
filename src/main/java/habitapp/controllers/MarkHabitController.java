package habitapp.controllers;

import habitapp.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс контроллера для отметки привычек.
 * Обрабатывает HTTP-запросы для отметки привычек как выполненных.
 */
@Tag(name = "Habit Management", description = "API for marking habits")
@Loggable
@RestController
@RequestMapping("/markhabit")
public class MarkHabitController {

    private final HabitsService habitsService; // Сервис для работы с привычками

    public MarkHabitController(HabitsService habitsService) {
        this.habitsService = habitsService;
    }

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
    @PutMapping
    public ResponseEntity<String> markHabit(
            @Parameter(description = "Request context") HttpServletRequest req,
            @Parameter(description = "Habit details", required = true, schema = @Schema(implementation = HabitDTO.class))
            @RequestBody HabitDTO habitDTO) {
        try {
            if (habitDTO == null) {
                return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                        .body("{\"message\": \"Habit cannot be null\"}");
            }

            habitsService.markHabit(req, habitDTO);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Habit marked successfully\"}");
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
    }
}