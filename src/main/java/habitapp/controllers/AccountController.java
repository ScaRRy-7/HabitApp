package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
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
 * Контроллер для управления аккаунтом пользователя.
 * <p>
 * Этот класс обрабатывает HTTP-запросы, связанные с редактированием и удалением аккаунта пользователя.
 * Он использует сервис `UsersService` для выполнения операций над аккаунтом.
 * </p>
 */
@Setter
@Loggable
@WebServlet("/account")
public class AccountController extends HttpServlet {

    /**
     * Объект для работы с JSON.
     */
    private ObjectMapper objectMapper;

    /**
     * Сервис для управления пользователями.
     */
    private UsersService usersService;

    /**
     * Конструктор для инициализации контроллера аккаунта.
     */
    public AccountController() {
        objectMapper = new ObjectMapper();
        usersService = UsersService.getInstance();
    }

    /**
     * Обрабатывает HTTP PUT-запрос для редактирования аккаунта пользователя.
     *
     * @param req  HTTP-запрос.
     * @param resp HTTP-ответ.
     * @throws ServletException если возникает ошибка в процессе обработки.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            usersService.redactUser(req, objectMapper.readValue(req.getReader(), UserDTO.class));
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Account was redacted: \"" +
                    objectMapper.writeValueAsString(req.getSession().getAttribute("user")) + "}");
        } catch (IOException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }

    /**
     * Обрабатывает HTTP DELETE-запрос для удаления аккаунта пользователя.
     *
     * @param req  HTTP-запрос.
     * @param resp HTTP-ответ.
     * @throws ServletException если возникает ошибка в процессе обработки.
     * @throws IOException      если возникает ошибка ввода-вывода.
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            usersService.removeAccount(req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Account removed\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }
}
