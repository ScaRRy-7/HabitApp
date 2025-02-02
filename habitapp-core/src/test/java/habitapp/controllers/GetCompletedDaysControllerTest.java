package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.HabitDTO;
import habitapp.dto.MessageDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.HabitsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetCompletedDaysControllerTest {

    @Mock
    private HabitsServiceImpl habitsServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private GetCompletedDaysController getCompletedDaysController;

    public GetCompletedDaysControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Успешное получение статистики привычек")
    public void testGetHabitStatistics_Success() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();
        List<LocalDateTime> completedDays = Arrays.asList(LocalDateTime.now());

        when(habitsServiceImpl.getCompletedDays(authHeader, habitDTO)).thenReturn(completedDays);
        when(objectMapper.writeValueAsString(any(MessageDTO.class))).thenReturn("{\"message\": \"[\"2023-10-01T10:00:00\"]\"}");

        ResponseEntity<String> response = getCompletedDaysController.getHabitStatistics(authHeader, habitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"[\"2023-10-01T10:00:00\"]\"}", response.getBody());
        verify(habitsServiceImpl).getCompletedDays(authHeader, habitDTO);
    }


    @Test
    @DisplayName("Обработка UserIllegalRequestException при получении статистики привычек")
    public void testGetHabitStatistics_UserIllegalRequestException() throws Exception {
        String authHeader = "Bearer token";
        HabitDTO habitDTO = new HabitDTO();
        when(habitsServiceImpl.getCompletedDays(authHeader, habitDTO)).thenThrow(new UserIllegalRequestException(HttpStatus.BAD_REQUEST.value(), "Некорректный запрос"));
        when(objectMapper.writeValueAsString(any(MessageDTO.class))).thenReturn("{\"message\": \"Некорректный запрос\"}");

        ResponseEntity<String> response = getCompletedDaysController.getHabitStatistics(authHeader, habitDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Некорректный запрос\"}", response.getBody());
    }


}