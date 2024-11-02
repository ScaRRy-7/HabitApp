package habitapp.controllers;

import habitapp.annotations.Loggable;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Loggable
@RestController
@RequestMapping("/exit")
public class ExitController  {

    public ExitController() {}

    @GetMapping
    public ResponseEntity<String> exitAccount(HttpServletRequest req)  {

        // Проверка, есть ли пользователь в сессии
        if (req.getSession().getAttribute("user") != null) {
            // Удаление пользователя из сессии
            req.getSession().removeAttribute("user");
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"exit successful\"}");
        } else {
            // Пользователь не авторизован
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"you are not logged in\"}");
        }
    }
}