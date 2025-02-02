package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dto.MessageDTO;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

/**
 * Контроллер для управления аккаунтом пользователя.
 * Предоставляет API для редактирования и удаления аккаунта.
 */
@Tag(name = "Account Management", description = "API для управления аккаунтом")
@RestController
@RequestMapping("/habitapp")
@Loggable
public class AccountController {

    private final UsersServiceImpl usersServiceImpl;
    private final ObjectMapper objectMapper;

    /**
     * Конструктор для инициализации контроллера.
     *
     * @param usersServiceImpl Сервис для работы с пользователями.
     * @param objectMapper     Объект для преобразования объектов в JSON.
     */
    public AccountController(UsersServiceImpl usersServiceImpl, ObjectMapper objectMapper) {
        this.usersServiceImpl = usersServiceImpl;
        this.objectMapper = objectMapper;
    }

    /**
     * Редактирует информацию об аккаунте.
     *
     * @param authHeader Заголовок авторизации.
     * @param userDTO   Объект с данными пользователя для редактирования.
     * @return Ответ с сообщением о результате операции.
     * @throws JsonProcessingException В случае ошибки при обработке JSON.
     */
    @Operation(
            summary = "Edit account details",
            description = "Redacts the account info"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully redacted"),
            @ApiResponse(responseCode = "401", description = "User  is not unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid JSON")
    })
    @PutMapping("editaccount")
    public ResponseEntity<String> editAccount(@RequestHeader("Authorization") String authHeader, @RequestBody UserDTO userDTO) throws JsonProcessingException {
        MessageDTO messageDTO;
        try {
            usersServiceImpl.redactUser (authHeader, userDTO);
            messageDTO = new MessageDTO("{\"message\": \"Account was redacted\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode()).body(jsonResponse);
        } catch (HttpMessageNotReadableException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.badRequest().body(jsonResponse);
        }
    }

    /**
     * Удаляет аккаунт пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @return Ответ с сообщением о результате операции.
     * @throws JsonProcessingException В случае ошибки при обработке JSON.
     */
    @Operation(
            summary = "Delete account",
            description = "Delete account which user logged in"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully deleted"),
            @ApiResponse(responseCode = "401", description = "User  is not unauthorized")
    })
    @DeleteMapping("/deleteaccount")
    public ResponseEntity<String> deleteAccount(@RequestHeader("Authorization") String authHeader) throws JsonProcessingException {
        MessageDTO messageDTO;
        try {
            usersServiceImpl.removeAccount(authHeader);
            messageDTO = new MessageDTO("{\"message\": \"Account was deleted\"}");
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.ok(jsonResponse);
        } catch (UserIllegalRequestException e) {
            messageDTO = new MessageDTO(e.getMessage());
            String jsonResponse = objectMapper.writeValueAsString(messageDTO);
            return ResponseEntity.status(e.getErrorCode()).body(jsonResponse);
        }
    }
}