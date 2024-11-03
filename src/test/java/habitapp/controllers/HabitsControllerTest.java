package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.HabitDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HabitsControllerTest {

    @InjectMocks
    private HabitsController habitsController;

    @Mock
    private HabitsService habitsService;

    @Mock
    private HttpServletRequest req;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        habitsController = new HabitsController(habitsService, objectMapper);
    }

    @Test
    void createHabit_Success() throws Exception {
        HabitDTO habitDTO = new HabitDTO();
        doNothing().when(habitsService).createHabit(any(), any());

        ResponseEntity<String> response = habitsController.createHabit(req, habitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Habit created successfully\"}", response.getBody());
    }

    @Test
    void createHabit_InvalidJson() throws Exception {
        doThrow(new HttpMessageNotReadableException("Invalid JSON")).when(habitsService).createHabit(any(), any());

        ResponseEntity<String> response = habitsController.createHabit(req, new HabitDTO());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Incorrect json (Invalid JSON)\"}", response.getBody());
    }

    @Test
    void createHabit_Unauthorized() throws Exception {
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "User is not authorized"))
                .when(habitsService).createHabit(any(), any());

        ResponseEntity<String> response = habitsController.createHabit(req, new HabitDTO());

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not authorized", response.getBody());
    }

    @Test
    void getHabits_Success() throws Exception {
        List<HabitDTO> habits = Arrays.asList(new HabitDTO(), new HabitDTO());
        when(habitsService.getAllHabits(any())).thenReturn(habits);

        ResponseEntity<String> response = habitsController.getHabits(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(objectMapper.writeValueAsString(habits), response.getBody());
    }

    @Test
    void getHabits_Unauthorized() throws Exception {
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "User is not authorized"))
                .when(habitsService).getAllHabits(any());

        ResponseEntity<String> response = habitsController.getHabits(req);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User is not authorized", response.getBody());
    }

    @Test
    void deleteHabit_Success() throws Exception {
        List<HabitDTO> remainingHabits = Arrays.asList(new HabitDTO());
        doNothing().when(habitsService).deleteHabit(any(), any());
        when(habitsService.getAllHabits(any())).thenReturn(remainingHabits);

        ResponseEntity<String> response = habitsController.deleteHabit(req, new HabitDTO());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(objectMapper.writeValueAsString(remainingHabits), response.getBody());
    }

    @Test
    void deleteHabit_InvalidJson() throws Exception {
        doThrow(new HttpMessageNotReadableException("Invalid JSON")).when(habitsService).deleteHabit(any(), any());

        ResponseEntity<String> response = habitsController.deleteHabit(req, new HabitDTO());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Incorrect json: Invalid JSON\"}", response.getBody());
    }

    @Test
    void deleteHabit_NotFound() throws Exception {
        doThrow(new UserIllegalRequestException(HttpStatus.NOT_FOUND.value(), "Habit not found"))
                .when(habitsService).deleteHabit(any(), any());

        ResponseEntity<String> response = habitsController.deleteHabit(req, new HabitDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Habit not found", response.getBody());
    }

    @Test
    void editHabit_Success() throws Exception {
        HabitDTO habitDTO1 = new HabitDTO();
        HabitDTO habitDTO2 = new HabitDTO();
        doNothing().when(habitsService).redactHabit(any(), any());

        ResponseEntity<String> response = habitsController.editHabit(req, habitDTO1, habitDTO2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Habit successfully changed\"}", response.getBody());
    }

    @Test
    void editHabit_InvalidJson() throws Exception {
        doThrow(new HttpMessageNotReadableException("Invalid JSON")).when(habitsService).redactHabit(any(), any());

        ResponseEntity<String> response = habitsController.editHabit(req, new HabitDTO(), new HabitDTO());

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Incorrect json: Invalid JSON\"}", response.getBody());
    }

    @Test
    void editHabit_NotFound() throws Exception {
        doThrow(new UserIllegalRequestException(HttpStatus.NOT_FOUND.value(), "Habit not found"))
                .when(habitsService).redactHabit(any(), any());

        ResponseEntity<String> response = habitsController.editHabit(req, new HabitDTO(), new HabitDTO());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Habit not found", response.getBody());
    }
}