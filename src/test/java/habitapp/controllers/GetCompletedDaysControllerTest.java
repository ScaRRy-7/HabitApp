package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.dto.HabitDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetCompletedDaysControllerTest {

    private GetCompletedDaysController getCompletedDaysController;
    private HabitsService habitsService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        habitsService = mock(HabitsService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        when(response.getWriter()).thenReturn(writer);

        getCompletedDaysController = new GetCompletedDaysController();
        getCompletedDaysController.setHabitsService(habitsService);
    }

    @Test
    void testDoGet_UserIllegalRequestException() throws Exception {
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setName("Test Habit");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(habitDTO))));

        when(habitsService.getCompletedDays(any(HttpServletRequest.class), any(HabitDTO.class)))
                .thenThrow(new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid request"));

        getCompletedDaysController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("\"Invalid request\"");
    }

    @Test
    void testDoGet_IOException() throws Exception {
        when(request.getReader()).thenThrow(new IOException("Reader error"));

        getCompletedDaysController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("\"Reader error\"");
    }
}