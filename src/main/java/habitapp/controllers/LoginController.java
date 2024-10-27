package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.annotaions.Loggable;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;

/**
 * Класс контроллера для обработки запросов на вход пользователей.
 * Обрабатывает HTTP-запросы для аутентификации пользователей.
 */
@Setter
@Loggable
@WebServlet("/login")
public class LoginController extends HttpServlet {

    /**
     * Конструктор контроллера входа.
     * Инициализирует сервис пользователей и объект маппера для работы с JSON.
     */
    public LoginController() {
        this.usersService = UsersService.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    private UsersService usersService; // Сервис для работы с пользователями
    private ObjectMapper objectMapper; // Объект для сериализации и десериализации JSON

    /**
     * Обрабатывает POST-запрос для входа пользователя в систему.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
            usersService.loginUser (userDTO, req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"authorized\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write("{\"message\": \"authorized failed (" + e.getMessage() + ")\"}");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        }
    }
}