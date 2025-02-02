package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dto.MessageDTO;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для регистрации пользователей.
 * Предоставляет API для операций регистрации пользователей.
 */
@Tag(name = "User  Registration", description = "API for user registration operations")
@RestController
@RequestMapping("/habitapp")
@Slf4j
@Loggable
public class RegisterController {

    private final UsersServiceImpl usersServiceImpl;
    private final ObjectMapper objectMapper;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param usersServiceImpl Сервис для работы с пользователями.
     * @param objectMapper     Объект для преобразования объектов в JSON.
     */
    public RegisterController(UsersServiceImpl usersServiceImpl, ObjectMapper objectMapper) {
        this.usersServiceImpl = usersServiceImpl;
        this.objectMapper = objectMapper;
    }

    /**
     * Регистрирует нового пользователя с предоставленной информацией.
     *
     * @param userDTO Объект с данными для регистрации пользователя.
     * @return Ответ с сообщением о результате регистрации и токеном.
     * @throws JsonProcessingException В случае ошибки при обработке JSON.
     */
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
    @PostMapping("/register")
    public ResponseEntity<String> register(
            @Parameter(description = "User  registration details", required = true, schema = @Schema(implementation = UserDTO.class))
            @RequestBody UserDTO userDTO) throws JsonProcessingException {

        MessageDTO messageDTO;
        try {
            String token = usersServiceImpl.registerUser (userDTO);
            messageDTO = new MessageDTO("User  registered successfully, token: " + token);
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