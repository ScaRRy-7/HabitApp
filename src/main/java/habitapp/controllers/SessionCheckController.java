package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.annotaions.Loggable;
import habitapp.dto.UserDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Класс контроллера для проверки состояния сессии пользователя.
 * Обрабатывает HTTP-запросы для проверки, авторизован ли пользователь.
 */
@Loggable
@WebServlet("/checksession")
public class SessionCheckController extends HttpServlet {

    private final ObjectMapper objectMapper = new ObjectMapper(); // Объект для сериализации и десериализации JSON

    /**
     * Обрабатывает GET-запрос для проверки состояния сессии пользователя.
     *
     * @param req  HTTP-запрос
     * @param resp HTTP-ответ
     * @throws ServletException если возникает ошибка сервлета
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user"); // Получаем пользователя из сессии

        if (userDTO != null) {
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"user logged in: " + userDTO.getEmail() + "\"}");
        } else {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"message\": \"user not logged in\"}");
        }
    }
}