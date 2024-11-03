package habitapp.controllers;

import habitapp.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class SessionCheckControllerTest {

    @InjectMocks
    private SessionCheckController sessionCheckController;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(request.getSession()).thenReturn(session);
    }

    @Test
    void checkSession_UserLoggedIn() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("user@example.com");
        when(session.getAttribute("user")).thenReturn(userDTO);

        ResponseEntity<String> response = sessionCheckController.checkSession(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"user logged in: user@example.com\"}", response.getBody());
    }

    @Test
    void checkSession_UserNotLoggedIn() {
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<String> response = sessionCheckController.checkSession(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"user not logged in\"}", response.getBody());
    }

    @Test
    void checkSession_NullSession() {
        // Подготовка сессии без пользователя
        when(session.getAttribute("user")).thenReturn(null);

        ResponseEntity<String> response = sessionCheckController.checkSession(request);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"user not logged in\"}", response.getBody());
    }

    @Test
    void checkSession_EmptyUserDTO() {
        when(session.getAttribute("user")).thenReturn(new UserDTO());

        ResponseEntity<String> response = sessionCheckController.checkSession(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"user logged in: null\"}", response.getBody());
    }
}