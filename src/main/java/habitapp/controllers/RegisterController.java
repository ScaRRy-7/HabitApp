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

import java.io.IOException;

/**
 * Класс контроллера для обработки запросов на регистрацию пользователей.
 * Обрабатывает HTTP-запросы для регистрации новых пользователей.
 */
@Loggable
@WebServlet("/register")
public class RegisterController extends HttpServlet {

    /**
     * Конструктор контроллера регистрации.
     * Инициализирует сервис пользователей и объект маппера для работы с JSON.
     */
    public RegisterController() {
        this.usersService = UsersService.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Setter
    private UsersService usersService; // Сервис для работы с пользователями
    private final ObjectMapper objectMapper; // Объект для сериализации и десериализации JSON

    /**
     * Обрабатывает POST-запрос для регистрации нового пользователя.
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
            usersService.registerUser (userDTO, req);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"User registered successfully\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write("{\"message\": \"Registration failed (" + e.getMessage() + ")\"}");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        }
    }
}