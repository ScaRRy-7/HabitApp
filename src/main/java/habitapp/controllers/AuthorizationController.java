package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/login")
public class AuthorizationController extends HttpServlet {

    private final UserService userService;
    private final ObjectMapper objectMapper;

    public AuthorizationController() {
        this.userService = UserService.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            UserDTO userDTO = objectMapper.readValue(req.getReader(), UserDTO.class);
            userService.loginUser(userDTO, req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"authorized\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write("{\"message\": \"authorized failed (" + e.getMessage() + ")\"}");
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        }
    }
}
