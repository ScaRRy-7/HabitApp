package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.annotations.Loggable;
import habitapp.dto.MessageDTO;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.jwt.JwtUtil;
import habitapp.services.UsersServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RestController
@RequestMapping("/register")
@Slf4j
public class RegisterController {

    private final UsersServiceImpl usersServiceImpl; // Сервис для работы с пользователями
    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;

    public RegisterController(UsersServiceImpl usersServiceImpl, ObjectMapper objectMapper, JwtUtil jwtUtil) {
        this.usersServiceImpl = usersServiceImpl;
        this.objectMapper = objectMapper;
        this.jwtUtil = jwtUtil;
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
            @Parameter(description = "User registration details", required = true, schema = @Schema(implementation = UserDTO.class))
            @RequestBody UserDTO userDTO) throws JsonProcessingException {
        MessageDTO messageDTO;
        try {
            String token = usersServiceImpl.registerUser(userDTO);
            messageDTO = new MessageDTO("User registered successfully, token: " + token);
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode())
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (HttpMessageNotReadableException e) {
            messageDTO = new MessageDTO("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        }
    }
}