package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.time.LocalDateTime;
import java.util.List;

/**
 * GetCompletedDaysController - сервлет, который обрабатывает HTTP-запросы для получения статистики
 * о выполненных днях для определенной привычки
 */
@Loggable
@WebServlet("/statistics")
public class GetCompletedDaysController extends HttpServlet {

    @Setter
    private HabitsService habitsService;
    private final ObjectMapper objectMapper;

    /**
     * Конструктор GetCompletedDaysController, который инициализирует HabitsService и ObjectMapper
     * ObjectMapper настраивается для работы с датами и временем
     */
    public GetCompletedDaysController() {
        this.habitsService = HabitsService.getInstance();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Обрабатывает HTTP GET запросы для получения списка выполненных дней для привычки
     *
     * @param req  объект HttpServletRequest, который содержит запрос, сделанный клиентом
     * @param resp объект HttpServletResponse, который будет использоваться для возврата ответа клиенту
     * @throws ServletException если запрос на GET не может быть обработан
     * @throws IOException      если возникает ошибка ввода-вывода во время обработки запроса GET
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO postedHabit = objectMapper.readValue(req.getReader(), HabitDTO.class);

            List<LocalDateTime> habitDTOList = habitsService.getCompletedDays(req, postedHabit);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(habitDTOList));
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(objectMapper.writeValueAsString(e.getMessage()));
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write(objectMapper.writeValueAsString(e.getMessage()));
        }
    }
}