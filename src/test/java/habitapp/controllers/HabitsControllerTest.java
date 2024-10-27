package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import habitapp.dto.HabitDTO;
import habitapp.services.HabitsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class HabitsControllerTest {

    private HabitsController habitsController;
    private HabitsService habitsService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;
    private HabitDTO habitDTO;

    @BeforeEach
    public void setUp() throws IOException {
        habitsService = mock(HabitsService.class);
        habitsController = new HabitsController();
        habitsController.setHabitsService(habitsService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        PrintWriter printWriter = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(printWriter);
        habitDTO = new HabitDTO();
    }

    @Test
    public void testDoPost_InvalidJson() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("invalid json")));

        habitsController.doPost(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).write(contains("Incorrect json"));
    }

    @Test
    public void testDoGet_Success() throws Exception {
        List<HabitDTO> habitList = Collections.singletonList(habitDTO);
        when(habitsService.getAllHabits(request)).thenReturn(habitList);

        habitsController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response.getWriter()).write(captor.capture());
        assertEquals(objectMapper.writeValueAsString(habitList), captor.getValue());
    }

    @Test
    public void testDoDelete_Success() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(habitDTO))));

        habitsController.doDelete(request, response);
        verify(response).setStatus(HttpServletResponse.SC_OK);
    }


    @Test
    public void testDoPut_InvalidJson() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("invalid json")));

        habitsController.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter ()).write(contains("Incorrect json"));
    }
}