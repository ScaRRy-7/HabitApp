package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Класс контроллера для отметки привычек.
 * Обрабатывает HTTP-запросы для отметки привычек как выполненных.
 */
@Loggable
@WebServlet("/markhabit")
@RestController
@RequestMapping("/markhabit")
public class MarkHabitController extends HttpServlet {

    public MarkHabitController(HabitsService habitsService) {
        this.habitsService = habitsService;
    }

    private HabitsService habitsService; // Сервис для работы с привычками

    @PutMapping
    public ResponseEntity<String> markHabit(HttpServletRequest req, @RequestBody HabitDTO habitDTO) {
        try {
            habitsService.markHabit(req, habitDTO);
            return ResponseEntity.ok("{\"message\": \"Habit marked successfully\"}");
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
    }
}