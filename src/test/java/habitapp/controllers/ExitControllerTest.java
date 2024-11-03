package habitapp.controllers;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ExitControllerTest {

    @InjectMocks
    private ExitController exitController;

    @Mock
    private HttpServletRequest req;

    @Mock
    private HttpSession session;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(req.getSession()).thenReturn(session); // Настройка, чтобы getSession возвращал имитацию сессии
    }

    @Test
    public void testExitAccount_Success() {
        // Подготовка сессии с пользователем
        when(session.getAttribute("user")).thenReturn(new Object());

        ResponseEntity<String> response = exitController.exitAccount(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"exit successful\"}", response.getBody());

        // Проверка, что пользователь был удален из сессии
        verify(session).removeAttribute("user");
    }

    @Test
    public void testExitAccount_UserNotLoggedIn() {
        // Подготовка сессии без пользователя
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<String> response = exitController.exitAccount(req);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\": \"you are not logged in\"}", response.getBody());

        // Проверка, что метод removeAttribute не был вызван
        verify(session, never()).removeAttribute("user");
    }
}