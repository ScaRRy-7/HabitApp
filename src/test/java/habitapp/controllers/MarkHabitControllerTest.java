package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.dto.HabitDTO;
import habitapp.enums.HabitFrequency;
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

class MarkHabitControllerTest {

    private MarkHabitController markHabitController;
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
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        writer = mock(PrintWriter.class);

        when(response.getWriter()).thenReturn(writer);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));

        markHabitController = new MarkHabitController();
        markHabitController.setHabitsService(habitsService);
    }

    @Test
    void testDoPut_Success() throws Exception {
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setName("Test Habit");
        habitDTO.setDescription("Description of the test habit");
        habitDTO.setFrequency(HabitFrequency.DAILY);
        habitDTO.setCompleted(false);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(habitDTO))));
        doNothing().when(habitsService).markHabit(any(HttpServletRequest.class), any(HabitDTO.class));

        markHabitController.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write("{\"message\": \"Habit marked successfully\"}");
    }

    @Test
    void testDoPut_JsonProcessingException() throws Exception {
        when(request.getReader()).thenThrow(new IOException("Reader error"));

        markHabitController.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("{\"message\": \"Incorrect json (Reader error)\"}");
    }

    @Test
    void testDoPut_UserIllegalRequestException() throws Exception {
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setName("Test Habit");
        habitDTO.setDescription("Description of the test habit");
        habitDTO.setFrequency(HabitFrequency.DAILY);
        habitDTO.setCompleted(false);

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(habitDTO))));
        doThrow(new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "User  not authorized"))
                .when(habitsService).markHabit(any(HttpServletRequest.class), any(HabitDTO.class));

        markHabitController.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(writer).write("User  not authorized");
    }
}