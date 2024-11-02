package habitapp.controllers;

import habitapp.annotations.Loggable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Exit", description = "API для выхода из аккаунта")
@Loggable
@RestController
@RequestMapping("/exit")
public class ExitController  {

    public ExitController() {}

    @Operation(
            summary = "Exit account",
            description = "Exit account which user logged in"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Account successfully exited",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "User is not authorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = String.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<String> exitAccount(HttpServletRequest req)  {

        // Проверка, есть ли пользователь в сессии
        if (req.getSession().getAttribute("user") != null) {
            // Удаление пользователя из сессии
            req.getSession().removeAttribute("user");
            return ResponseEntity.ok().body("{\"message\": \"exit successful\"}");
        } else {
            // Пользователь не авторизован
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"you are not logged in\"}");
        }
    }
}