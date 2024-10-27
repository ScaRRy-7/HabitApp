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

import java.io.IOException;

/**
 * Класс контроллера для отметки привычек.
 * Обрабатывает HTTP-запросы для отметки привычек как выполненных.
 */
@Loggable
@WebServlet("/markhabit")
public class MarkHabitController extends HttpServlet {

    /**
     * Конструктор контроллера отметки привычек.
     * Инициализирует сервис привычек и объект маппера для работы с JSON.
     */
    public MarkHabitController() {
        this.habitsService = HabitsService.getInstance();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Setter
    private HabitsService habitsService; // Сервис для работы с привычками
    private final ObjectMapper objectMapper; // Объект для сериализации и десериализации JSON

    /**
     * Обрабатывает PUT-запрос для отметки привычки как выполненной.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO habitDTO = objectMapper.readValue(req.getReader(), HabitDTO.class);
            habitsService.markHabit(req, habitDTO);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Habit marked successfully\"}");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }
}