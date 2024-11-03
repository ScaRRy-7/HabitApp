package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsService;
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

@Tag(name = "Statistics", description = "API для получения статистики привычек")
@Loggable
@RestController
@RequestMapping("/statistics")
public class GetCompletedDaysController {

    private HabitsService habitsService;
    private final ObjectMapper objectMapper;

    public GetCompletedDaysController(HabitsService habitsService, ObjectMapper objectMapper) {
        this.habitsService = habitsService;
        this.objectMapper = objectMapper;

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

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
                    description = "User is not authorized",
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
    @PostMapping
    public ResponseEntity<String> getHabitStatistics(HttpServletRequest req, @RequestBody HabitDTO habitDTO) {
        if (habitDTO == null) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Habit cannot be null\"}");
        }

        try {
            List<LocalDateTime> habitDTOList = habitsService.getCompletedDays(req, habitDTO);
            return ResponseEntity.ok(objectMapper.writeValueAsString(habitDTOList));
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        }
    }
}