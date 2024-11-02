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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * Класс контроллера для управления привычками.
 * Обрабатывает HTTP-запросы, связанные с созданием, получением, удалением и изменением привычек.
 */
@Loggable
@RestController
@RequestMapping("/habits")
public class HabitsController {

    /**
     * Конструктор контроллера привычек.
     * Инициализирует сервис привычек и объект маппера для работы с JSON.
     */
    public HabitsController(HabitsService habitsService, ObjectMapper objectMapper) {
        this.habitsService = habitsService;
        this.objectMapper = objectMapper;
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Setter
    private HabitsService habitsService; // Сервис для работы с привычками
    private final ObjectMapper objectMapper; // Объект для сериализации и десериализации JSON


    @PostMapping
    public ResponseEntity<String> createHabit(HttpServletRequest req, @RequestBody HabitDTO habitDTO) {
        try {
            habitsService.createHabit(req, habitDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Habit created successfully\"}");
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).contentType(MediaType.APPLICATION_JSON)
                    .body(e.getMessage());
        }
    }

    @GetMapping
        public ResponseEntity<String> getHabits(HttpServletRequest req) {
        try {
            List<HabitDTO> habitDTOList = habitsService.getAllHabits(req);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(habitDTOList));
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


    @DeleteMapping
    public ResponseEntity<String> deleteHabit(HttpServletRequest req, @RequestBody HabitDTO habitDTO) throws IOException {
        try {
            habitsService.deleteHabit(req, habitDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body(objectMapper.writeValueAsString(habitsService.getAllHabits(req)));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"Incorrect Json: \": \"" + e.getMessage() + "\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        }
    }


    @PutMapping
    public ResponseEntity<String> doPut(HttpServletRequest req, @RequestBody HabitDTO habitDTO1, @RequestBody HabitDTO habitDTO2)
    {
        try {
            HabitDTO[] habitDTOs = new HabitDTO[] {habitDTO1, habitDTO2};
            habitsService.redactHabit(req, habitDTOs);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Habit successfully changed\"}");
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        }
    }
}