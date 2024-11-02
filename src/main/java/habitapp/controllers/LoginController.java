package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Класс контроллера для обработки запросов на вход пользователей.
 * Обрабатывает HTTP-запросы для аутентификации пользователей.
 */
@Loggable
@RestController
@RequestMapping("/login")
public class LoginController {


    public LoginController(UsersService usersService) {
        this.usersService = usersService;
    }

    private UsersService usersService; // Сервис для работы с пользователями

    @PostMapping
    public ResponseEntity<String> login(HttpServletRequest req, @RequestBody UserDTO userDTO) {
        try {
            usersService.loginUser (userDTO, req);
            return ResponseEntity.ok("{\"message\": \"authorized\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        }
    }
}