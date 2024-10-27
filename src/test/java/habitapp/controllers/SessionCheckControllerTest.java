package habitapp.controllers;

import habitapp.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.io.PrintWriter;

import static org.mockito.Mockito.*;

class SessionCheckControllerTest {

    private SessionCheckController sessionCheckController;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private PrintWriter writer;

    @BeforeEach
    void setUp() throws IOException {
        sessionCheckController = new SessionCheckController();
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }

    @Test
    void testDoGet_UserLoggedIn() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getSession().getAttribute("user")).thenReturn(userDTO);

        sessionCheckController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(writer).write("{\"message\": \"user logged in: test@example.com\"}");
    }

    @Test
    void testDoGet_UserNotLoggedIn() throws Exception {
        when(request.getSession()).thenReturn(mock(HttpSession.class));
        when(request.getSession().getAttribute("user")).thenReturn(null);

        sessionCheckController.doGet(request, response);

        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        verify(writer).write("{\"message\": \"user not logged in\"}");
    }
}