//
//package habitapp.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import habitapp.dto.UserDTO;
//import habitapp.exceptions.UserIllegalRequestException;
//import habitapp.services.UsersService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringReader;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class LoginControllerTest {
//
//    private LoginController loginController;
//    private UsersService usersService;
//    private HttpServletRequest request;
//    private HttpServletResponse response;
//    private ObjectMapper objectMapper;
//    private PrintWriter writer;
//
//    @BeforeEach
//    void setUp() throws IOException {
//        usersService = mock(UsersService.class);
//        request = mock(HttpServletRequest.class);
//        response = mock(HttpServletResponse.class);
//        writer = mock(PrintWriter.class);
//        objectMapper = new ObjectMapper();
//        loginController = new LoginController();
//        loginController.setObjectMapper(objectMapper);
//        loginController.setUsersService(usersService);
//
//        when(response.getWriter()).thenReturn(writer);
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
//    }
//
//    @Test
//    void testDoPost_Success() throws Exception {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("Test User");
//        userDTO.setEmail("user@example.com");
//        userDTO.setPassword("password");
//
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDTO))));
//        doNothing().when(usersService).loginUser (any(UserDTO.class), any(HttpServletRequest.class));
//
//        loginController.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_OK);
//        verify(response.getWriter()).write("{\"message\": \"authorized\"}");
//    }
//
//    @Test
//    void testDoPost_UserIllegalRequestException() throws Exception {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("Test User");
//        userDTO.setEmail("user@example.com");
//        userDTO.setPassword("password");
//
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDTO))));
//        doThrow(new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or password or name"))
//                .when(usersService).loginUser (any(UserDTO.class), any(HttpServletRequest.class));
//
//        loginController.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        verify(response.getWriter()).write("{\"message\": \"authorized failed (Invalid email or password or name)\"}");
//    }
//
//    @Test
//    void testDoPost_JsonProcessingException() throws Exception {
//        when(request.getReader()).thenThrow(new IOException("Reader error"));
//
//        loginController.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        verify(response.getWriter()).write("{\"message\": \"Incorrect json (Reader error)\"}");
//    }
//}