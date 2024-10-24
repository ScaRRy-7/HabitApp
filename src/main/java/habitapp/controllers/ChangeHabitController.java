package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.entities.Habit;
import habitapp.services.HabitService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/removehabit")
public class RemoveHabitController extends HttpServlet {

    private final HabitService habitService;
    private final ObjectMapper objectMapper;

    public RemoveHabitController() {
        this.habitService = HabitService.getInstance();
        this.objectMapper = new ObjectMapper();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        habitService.redactHabit(objectMapper.readValue(req.getReader(), Habit.class));
    }
}
