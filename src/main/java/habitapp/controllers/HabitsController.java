package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.MessageDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;


@Tag(name = "Habits", description = "API for managing user habits")
@RestController
@RequestMapping("/habitapp")
public class HabitsController {

    @Setter
    private HabitsServiceImpl habitsServiceImpl;
    private final ObjectMapper objectMapper;

    public HabitsController(HabitsServiceImpl habitsServiceImpl, ObjectMapper objectMapper) {
        this.habitsServiceImpl = habitsServiceImpl;
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Operation(
            summary = "Create a new habit",
            description = "Creates a new habit for an authorized user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Habit successfully created",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\"message\": \"Habit created successfully\"}")
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authorized",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\"message\": \"User is not authorized\"}")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid JSON format",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\"message\": \"Incorrect json\"}")
            )
    )
    @PostMapping("/habits")
    public ResponseEntity<String> createHabit(
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Habit details", required = true) @RequestBody HabitDTO habitDTO
    ) {
        try {
            habitsServiceImpl.createHabit(authHeader, habitDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Habit created successfully\"}");
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
    }

    @Operation(
            summary = "Get all habits",
            description = "Retrieves all habits for the authorized user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "List of habits successfully retrieved",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = HabitDTO.class))
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authorized",
            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    )
    @GetMapping("/habits")
    public ResponseEntity<String> getHabits(
            @RequestHeader("Authorization") String authHeader
    ) throws JsonProcessingException {
        MessageDTO messageDTO;
        try {
            List<HabitDTO> habitDTOList = habitsServiceImpl.getAllHabits(authHeader);
            messageDTO = new MessageDTO(objectMapper.writeValueAsString(habitDTOList));
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode()).body(jsonResponse);
        }
    }

    @Operation(
            summary = "Delete a habit",
            description = "Deletes the specified habit for the authorized user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Habit successfully deleted",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    array = @ArraySchema(schema = @Schema(implementation = HabitDTO.class))
            )
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authorized"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Habit not found"
    )
    @DeleteMapping("/habits")
    public ResponseEntity<String> deleteHabit(
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Habit to delete", required = true) @RequestBody HabitDTO habitDTO
    ) throws IOException {
        MessageDTO messageDTO;
        try {
            habitsServiceImpl.deleteHabit(authHeader, habitDTO);
            List<HabitDTO> habits = habitsServiceImpl.getAllHabits(authHeader);
            messageDTO = new MessageDTO(habits.toString());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (HttpMessageNotReadableException e) {
            messageDTO = new MessageDTO("{\"message\": \"Incorrect json: " + e.getMessage() + "\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(jsonResponse);
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Edit a habit",
            description = "Modifies an existing habit for the authorized user"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Habit successfully modified",
            content = @Content(
                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                    schema = @Schema(example = "{\"message\": \"Habit successfully changed\"}")
            )
    )
    @ApiResponse(
            responseCode = "400",
            description = "Invalid JSON format"
    )
    @ApiResponse(
            responseCode = "401",
            description = "User is not authorized"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Habit not found"
    )
    @PutMapping("/habits")
    public ResponseEntity<String> editHabit(
            @RequestHeader("Authorization") String authHeader,
            @Parameter(description = "Original habit", required = true) @RequestBody HabitDTO habitDTO1,
            @Parameter(description = "Updated habit", required = true) @RequestBody HabitDTO habitDTO2
    ) throws JsonProcessingException {
        MessageDTO messageDTO;
        try {
            HabitDTO[] habitDTOs = new HabitDTO[] {habitDTO1, habitDTO2};
            habitsServiceImpl.redactHabit(authHeader, habitDTOs);
            messageDTO = new MessageDTO("{\"message\": \"Habit successfully changed\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (HttpMessageNotReadableException e) {
            messageDTO = new MessageDTO("{\"message\": \"Incorrect json: " + e.getMessage() + "\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode()).body(jsonResponse);
        }
    }
}