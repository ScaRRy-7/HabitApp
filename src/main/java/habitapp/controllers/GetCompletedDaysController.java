package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.UserDTO;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * GetCompletedDaysController - сервлет, который обрабатывает HTTP-запросы для получения статистики
 * о выполненных днях для определенной привычки
 */
@Loggable
@RestController
@RequestMapping("/statistics")
public class GetCompletedDaysController {

    private HabitsService habitsService;
    private final ObjectMapper objectMapper;

    public GetCompletedDaysController(HabitsService habitsService, ObjectMapper objectMapper) {
        this.habitsService = habitsService;
        this.objectMapper = objectMapper;

        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    @PostMapping
    public ResponseEntity<String> getHabitStatistics(HttpServletRequest req, @RequestBody HabitDTO habitDTO) {
        try {
            List<LocalDateTime> habitDTOList = habitsService.getCompletedDays(req, habitDTO);
            return ResponseEntity.ok(objectMapper.writeValueAsString(habitDTOList));
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        } catch (HttpMessageNotReadableException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        }
    }
}