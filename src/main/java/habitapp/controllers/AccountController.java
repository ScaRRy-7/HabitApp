package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.annotaions.Loggable;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.controller.UserControllerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Loggable
@WebServlet("/account")
public class AccountController extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final UserControllerService userControllerService;

    public AccountController() {
        objectMapper = new ObjectMapper();
        userControllerService = UserControllerService.getInstance();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            userControllerService.redactUser(req, objectMapper.readValue(req.getReader(), UserDTO.class));
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Account was redacted: \"" +
                    objectMapper.writeValueAsString(req.getSession().getAttribute("user")) + "}");
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            userControllerService.removeAccount(req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Account removed\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }


}
