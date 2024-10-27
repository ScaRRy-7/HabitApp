package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.annotaions.Loggable;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Setter;

import java.io.IOException;

/**
 * Контроллер для обработки выхода пользователя из системы.
 * Удаляет пользователя из сессии и возвращает соответствующий ответ.
 */
@Loggable
@WebServlet("/exit")
public class ExitController extends HttpServlet {

    @Setter
    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     * Обрабатывает POST-запрос для выхода пользователя.
     *
     * @param req  HTTP-запрос.
     * @param resp HTTP-ответ.
     * @throws ServletException если происходит ошибка при обработке запроса.
     * @throws IOException      если происходит ошибка ввода-вывода.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Проверка, есть ли пользователь в сессии
        if (req.getSession().getAttribute("user") != null) {
            // Удаление пользователя из сессии
            req.getSession().removeAttribute("user");
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Exit successful\"}");
        } else {
            // Пользователь не авторизован
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resp.getWriter().write("{\"message\": \"Exit unauthorized\"}");
        }
    }
}