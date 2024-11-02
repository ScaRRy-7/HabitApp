package habitapp.controllers;

import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Класс контроллера для проверки состояния сессии пользователя.
 * Обрабатывает HTTP-запросы для проверки, авторизован ли пользователь.
 */
@Tag(name = "Session Management", description = "API for session management operations")
@Loggable
@RestController
@RequestMapping("/checksession")
public class SessionCheckController {

    @Operation(
            summary = "Check user session",
            description = "Checks if the user is logged in and returns session information"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Session successfully printed",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"user logged in: user@example.com\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User  is not authorized",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(example = "{\"message\": \"user not logged in\"}")
                    )
            )
    })
    @GetMapping
    public ResponseEntity<String> checkSession(@Parameter(description = "Request context") HttpServletRequest req) {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user"); // Получаем пользователя из сессии
        if (userDTO != null) {
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"user logged in: " + userDTO.getEmail() + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"user not logged in\"}");
        }
    }
}