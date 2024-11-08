package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dto.MessageDTO;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersServiceImpl;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контроллер для управления процессом входа пользователя.
 * Предоставляет API для аутентификации пользователей.
 */
@RestController
@RequestMapping("/habitapp")
@Loggable
public class LoginController {

    private final ObjectMapper objectMapper;
    private final UsersServiceImpl usersServiceImpl;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param usersServiceImpl Сервис для работы с пользователями.
     * @param objectMapper     Объект для преобразования объектов в JSON.
     */
    public LoginController(UsersServiceImpl usersServiceImpl, ObjectMapper objectMapper) {
        this.usersServiceImpl = usersServiceImpl;
        this.objectMapper = objectMapper;
    }

    /**
     * Выполняет аутентификацию пользователя.
     *
     * @param userDTO Объект с данными для аутентификации пользователя.
     * @return Ответ с токеном аутентификации или сообщением об ошибке.
     * @throws JsonProcessingException В случае ошибки при обработке JSON.
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(
            @Parameter(description = "User  authentication details", required = true,
                    schema = @Schema(implementation = UserDTO.class))
            @RequestBody UserDTO userDTO) throws JsonProcessingException {

        MessageDTO messageDTO;
        try {
            String token = usersServiceImpl.loginUser (userDTO);
            messageDTO = new MessageDTO("User  logged in successfully, token: " + token);
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        } catch (HttpMessageNotReadableException e) {
            messageDTO = new MessageDTO("Incorrect json (" + e.getMessage() + ")");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body(jsonResponse);
        }
    }
}