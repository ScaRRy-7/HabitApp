package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.UserDTO;
import habitapp.entities.User;
import habitapp.exceptions.UserException;
import habitapp.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/redactprofile")
public class RedactUserController extends HttpServlet {

    private final ObjectMapper objectMapper;
    private final UserService userService;

    public RedactUserController() {
        objectMapper = new ObjectMapper();
        userService = UserService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            userService.redactUser(req, objectMapper.readValue(req.getReader(), UserDTO.class));
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("Incorrect json: " + e.getMessage());
        } catch (UserException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }
}
