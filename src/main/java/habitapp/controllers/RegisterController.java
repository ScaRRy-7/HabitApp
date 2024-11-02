package habitapp.controllers;

import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс контроллера для обработки запросов на регистрацию пользователей.
 * Обрабатывает HTTP-запросы для регистрации новых пользователей.
 */
@Tag(name = "User  Registration", description = "API for user registration operations")
@Loggable
@RestController
@RequestMapping("/register")
public class RegisterController {

    private final UsersService usersService; // Сервис для работы с пользователями

    public RegisterController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(
            summary = "Register a new user",
            description = "Registers a new user with the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "User  registered successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"User  registered successfully\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid JSON format",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"Invalid JSON given\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "User  already exists",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"User  already exists\"}")
                    )
            )
    })
    @PostMapping
    public ResponseEntity<String> register(
            @Parameter(description = "Request context") HttpServletRequest req,
            @Parameter(description = "User  registration details", required = true, schema = @Schema(implementation = UserDTO.class))
            @RequestBody UserDTO userDTO) {
        try {
            usersService.registerUser (userDTO, req);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"User  registered successfully\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        }
    }
}