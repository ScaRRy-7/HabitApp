package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.annotaions.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.controller.HabitControllerService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Loggable
@WebServlet("/habits")
public class HabitsController extends HttpServlet {

    private final HabitControllerService habitControllerService;
    private final ObjectMapper objectMapper;

    public HabitsController() {
        this.habitControllerService = HabitControllerService.getInstance();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO habitDTO = objectMapper.readValue(req.getReader(), HabitDTO.class);
            habitControllerService.createHabit(req, habitDTO);
            resp.setStatus(HttpServletResponse.SC_CREATED);
            resp.getWriter().write("{\"message\": \"Habit created successfully\"}");
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            List<HabitDTO> habitDTOList = habitControllerService.getAllHabits(req);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(habitDTOList));
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(objectMapper.writeValueAsString(e.getMessage()));
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            resp.getWriter().write(objectMapper.writeValueAsString(e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO habitDTOtoRemove = objectMapper.readValue(req.getReader(), HabitDTO.class);
            habitControllerService.removeHabit(req, habitDTOtoRemove);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write(objectMapper.writeValueAsString(habitControllerService.getAllHabits(req)));
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"Incorrect Json: \": \"" + e.getMessage() + "\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write(e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            HabitDTO[] habitDTOs = objectMapper.readValue(req.getReader(), HabitDTO[].class);
            habitControllerService.redactHabit(req, habitDTOs);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.getWriter().write("{\"message\": \"Habit successfully changed\"}");
        } catch (JsonProcessingException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            resp.getWriter().write("{\"message\": \"Incorrect json (" + e.getMessage() + ")\"}");
        } catch (UserIllegalRequestException e) {
            resp.setStatus(e.getErrorCode());
            resp.getWriter().write("{\"message\": \"unauthorized (" + e.getMessage() + ")\"}");
        }
    }


}
