//package habitapp.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.io.IOException;
//import java.io.PrintWriter;
//import static org.mockito.Mockito.*;
//
//class ExitControllerTest {
//
//    private ExitController exitController;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private ObjectMapper objectMapper;
//    private HttpSession httpSession;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        exitController = new ExitController();
//        request = mock(HttpServletRequest.class);
//        response = mock(HttpServletResponse.class);
//        httpSession = mock(HttpSession.class);
//        objectMapper = new ObjectMapper();
//
//        exitController = new ExitController();
//        exitController.setObjectMapper(objectMapper);
//
//        when(request.getSession()).thenReturn(httpSession);
//        PrintWriter writer = mock(PrintWriter.class);
//        when(response.getWriter()).thenReturn(writer);
//    }
//
//    @Test
//    void testDoPost_WithUserInSession() throws Exception {
//        when(httpSession.getAttribute("user")).thenReturn("someUser");
//
//        exitController.doPost(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(response).setCharacterEncoding("UTF-8");
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(response.getWriter()).write("{\"message\": \"Exit successful\"}");
//    }
//
//    @Test
//    void testDoPost_WithoutUserInSession() throws Exception {
//        when(httpSession.getAttribute("user")).thenReturn(null);
//
//        exitController.doPost(request, response);
//
//        verify(response).setContentType("application/json");
//        verify(response).setCharacterEncoding("UTF-8");
//        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        verify(response.getWriter()).write("{\"message\": \"Exit unauthorized\"}");
//    }
//}