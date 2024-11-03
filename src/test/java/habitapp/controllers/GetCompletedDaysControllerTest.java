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
import org.springframework.http.ResponseEntity;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

public class GetCompletedDaysControllerTest {

    @InjectMocks
    private GetCompletedDaysController getCompletedDaysController;

    @Mock
    private HabitsService habitsService;

    @Mock
    private HttpServletRequest req;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        objectMapper = new ObjectMapper();
        getCompletedDaysController = new GetCompletedDaysController(habitsService, objectMapper);
    }

    @Test
    public void testGetHabitStatistics_Success() throws Exception {
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setName("Test Habit");

        List<LocalDateTime> completedDays = Arrays.asList(LocalDateTime.now(), LocalDateTime.now().minusDays(1));
        when(habitsService.getCompletedDays(req, habitDTO)).thenReturn(completedDays);

        ResponseEntity<String> response = getCompletedDaysController.getHabitStatistics(req, habitDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(objectMapper.writeValueAsString(completedDays), response.getBody());
    }

    @Test
    public void testGetHabitStatistics_UserNotAuthorized() throws Exception {
        HabitDTO habitDTO = new HabitDTO();
        habitDTO.setName("Test Habit");

        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "{\"message\": \"You are not authorized to view your habit statistics!\"}"))
                .when(habitsService).getCompletedDays(req, habitDTO);

        ResponseEntity<String> response = getCompletedDaysController.getHabitStatistics(req, habitDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\": \"You are not authorized to view your habit statistics!\"}", response.getBody());
    }

    @Test
    public void testGetHabitStatistics_InvalidJson() {
        // Передаем null вместо HabitDTO, чтобы вызвать ошибку при чтении JSON
        ResponseEntity<String> response = getCompletedDaysController.getHabitStatistics(req, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Habit cannot be null\"}", response.getBody());
    }
}