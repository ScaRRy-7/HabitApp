package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.HabitDTO;
import habitapp.dto.MessageDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class MarkHabitControllerTest {

    @Mock
    private HabitsServiceImpl habitsServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MarkHabitController markHabitController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Успешная пометка привычки")
    public void testMarkHabit_Success() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"Habit marked successfully\"}");

        ResponseEntity<String> response = markHabitController.markHabit(authHeader, habitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Habit marked successfully\"}", response.getBody());
        verify(habitsServiceImpl).markHabit(authHeader, habitDTO);
    }

    @Test
    @DisplayName("Ошибка 400 при некорректном JSON")
    public void testMarkHabit_BadRequest_InvalidJson() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new HttpMessageNotReadableException("Malformed JSON"))
                .when(habitsServiceImpl).markHabit(authHeader, habitDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"Incorrect json (Malformed JSON)\"}");

        ResponseEntity<String> response = markHabitController.markHabit(authHeader, habitDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Incorrect json (Malformed JSON)\"}", response.getBody());
    }

    @Test
    @DisplayName("Ошибка 401 при неавторизованном доступе")
    public void testMarkHabit_UserIllegalRequestException() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "User  is not authorized"))
                .when(habitsServiceImpl).markHabit(authHeader, habitDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"User  is not authorized\"}");

        ResponseEntity<String> response = markHabitController.markHabit(authHeader, habitDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\": \"User  is not authorized\"}", response.getBody());
    }
}