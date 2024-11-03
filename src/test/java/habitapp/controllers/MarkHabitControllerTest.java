package habitapp.controllers;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.doNothing;

class MarkHabitControllerTest {

    @InjectMocks
    private MarkHabitController markHabitController;

    @Mock
    private HabitsService habitsService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void markHabit_Success() {
        // Arrange
        HabitDTO habitDTO = new HabitDTO();
        doNothing().when(habitsService).markHabit(any(HttpServletRequest.class), any(HabitDTO.class));

        // Act
        ResponseEntity<String> response = markHabitController.markHabit(request, habitDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Habit marked successfully\"}", response.getBody());
    }

    @Test
    void markHabit_InvalidJson() {
        // Arrange
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new HttpMessageNotReadableException("Invalid JSON format"))
                .when(habitsService).markHabit(any(HttpServletRequest.class), any(HabitDTO.class));

        // Act
        ResponseEntity<String> response = markHabitController.markHabit(request, habitDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Incorrect json (Invalid JSON format)\"}", response.getBody());
    }

    @Test
    void markHabit_HabitNotFound() {
        // Arrange
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.NOT_FOUND.value(),
                "{\"message\": \"Habit not found\"}"))
                .when(habitsService).markHabit(any(HttpServletRequest.class), any(HabitDTO.class));

        // Act
        ResponseEntity<String> response = markHabitController.markHabit(request, habitDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Habit not found\"}", response.getBody());
    }

    @Test
    void markHabit_Unauthorized() {
        // Arrange
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(),
                "{\"message\": \"User is not authorized\"}"))
                .when(habitsService).markHabit(any(HttpServletRequest.class), any(HabitDTO.class));

        // Act
        ResponseEntity<String> response = markHabitController.markHabit(request, habitDTO);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"User is not authorized\"}", response.getBody());
    }

    @Test
    void markHabit_AlreadyMarked() {
        // Arrange
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.CONFLICT.value(),
                "{\"message\": \"Habit cannot be marked because it already marked\"}"))
                .when(habitsService).markHabit(any(HttpServletRequest.class), any(HabitDTO.class));

        // Act
        ResponseEntity<String> response = markHabitController.markHabit(request, habitDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Habit cannot be marked because it already marked\"}", response.getBody());
    }

    @Test
    void markHabit_NullHabitDTO() {
        // Act
        ResponseEntity<String> response = markHabitController.markHabit(request, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Habit cannot be null\"}", response.getBody());
    }
}