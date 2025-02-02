package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.HabitDTO;
import habitapp.dto.MessageDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HabitsControllerTest {

    @Mock
    private HabitsServiceImpl habitsServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private HabitsController habitsController;

    public HabitsControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Успешное создание привычки")
    public void testCreateHabit_Success() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();

        ResponseEntity<String> response = habitsController.createHabit(authHeader, habitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Habit created successfully\"}", response.getBody());
        verify(habitsServiceImpl).createHabit(authHeader, habitDTO);
    }

    @Test
    @DisplayName("Ошибка 400 при создании привычки с некорректным JSON")
    public void testCreateHabit_BadRequest() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = null; // некорректный объект

        ResponseEntity<String> response = habitsController.createHabit(authHeader, habitDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Incorrect json (null)\"}", response.getBody());
    }

    @Test
    @DisplayName("Обработка UserIllegalRequestException при создании привычки")
    public void testCreateHabit_UserIllegalRequestException() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "User  is not authorized"))
                .when(habitsServiceImpl).createHabit(authHeader, habitDTO);

        ResponseEntity<String> response = habitsController.createHabit(authHeader, habitDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User  is not authorized", response.getBody());
    }

    @Test
    @DisplayName("Обработка UserIllegalRequestException при удалении привычки")
    public void testDeleteHabit_UserIllegalRequestException() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.NOT_FOUND.value(), "Habit not found"))
                .when(habitsServiceImpl).deleteHabit(authHeader, habitDTO);

        ResponseEntity<String> response = habitsController.deleteHabit(authHeader, habitDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Habit not found", response.getBody());
    }

    @Test
    @DisplayName("Успешное редактирование привычки")
    public void testEditHabit_Success() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO1 = new HabitDTO();
        HabitDTO habitDTO2 = new HabitDTO();
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"Habit successfully changed\"}");

        ResponseEntity<String> response = habitsController.editHabit(authHeader, habitDTO1, habitDTO2);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Habit successfully changed\"}", response.getBody());
        verify(habitsServiceImpl).redactHabit(authHeader, new HabitDTO[]{habitDTO1, habitDTO2});
    }


}