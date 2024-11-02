package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Класс контроллера для проверки состояния сессии пользователя.
 * Обрабатывает HTTP-запросы для проверки, авторизован ли пользователь.
 */
@Loggable
@RestController
@RequestMapping("/checksession")
public class SessionCheckController {

    public ResponseEntity<String> checkSession(HttpServletRequest req) throws ServletException, IOException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user"); // Получаем пользователя из сессии
        if (userDTO != null) {
            return ResponseEntity.ok("{\"message\": \"user logged in: " + userDTO.getEmail() + "\"}");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"user not logged in\"}");
        }
    }
}