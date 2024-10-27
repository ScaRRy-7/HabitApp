package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.annotaions.Loggable;
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
import java.util.List;

/**
 * Класс контроллера для управления привычками.
 * Обрабатывает HTTP-запросы, связанные с созданием, получением, удалением и изменением привычек.
 */
@Loggable
@WebServlet("/habits")
public class HabitsController extends HttpServlet {

    /**
     * Конструктор контроллера привычек.
     * Инициализирует сервис привычек и объект маппера для работы с JSON.
     */
    public HabitsController() {
        this.habitsService = HabitsService.getInstance();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Setter
    private HabitsService habitsService; // Сервис для работы с привычками
    private final ObjectMapper objectMapper; // Объект для сериализации и десериализации JSON

    /**
     * Обрабатывает POST-запрос для создания новой привычки.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO habitDTO = objectMapper.readValue(req.getReader(), HabitDTO.class);
            habitsService.createHabit(req, habitDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"Habit created successfully\"}");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * Обрабатывает GET-запрос для получения списка всех привычек.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<HabitDTO> habitDTOList = habitsService.getAllHabits(req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(habitDTOList));
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(objectMapper.writeValueAsString(e.getMessage()));
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(e.getMessage()));
        }
    }

    /**
     * Обрабатывает DELETE-запрос для удаления привычки.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO habitDTOtoRemove = objectMapper.readValue(req.getReader(), HabitDTO.class);
            habitsService.deleteHabit(req, habitDTOtoRemove);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(habitsService.getAllHabits(req)));
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"Incorrect Json: \": \"" + e.getMessage() + "\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * Обрабатывает PUT-запрос для изменения привычек.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws IOException если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO[] habitDTOs = objectMapper.readValue(req.getReader(), HabitDTO[].class);
            habitsService.redactHabit(req, habitDTOs);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Habit successfully changed\"}");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write("{\"message\": \"unauthorized (" + e.getMessage() + ")\"}");
        }
    }
}