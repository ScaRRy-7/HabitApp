//package habitapp.controllers;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import habitapp.dto.UserDTO;
//import habitapp.exceptions.UserIllegalRequestException;
//import habitapp.services.UsersService;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.io.StringReader;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//class RegisterControllerTest {
//
//    private RegisterController registerController;
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
//        objectMapper = new ObjectMapper();
//        writer = mock(PrintWriter.class);
//
//        when(response.getWriter()).thenReturn(writer);
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{}")));
//
//        registerController = new RegisterController();
//        registerController.setUsersService(usersService);
//    }
//
//    @Test
//    void testDoPost_Success() throws Exception {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("testUser ");
//        userDTO.setPassword("testPassword");
//
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDTO))));
//        doNothing().when(usersService).registerUser (any(UserDTO.class), any(HttpServletRequest.class));
//
//        registerController.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_CREATED);
//        verify(writer).write("{\"message\": \"User registered successfully\"}");
//    }
//
//    @Test
//    void testDoPost_UserIllegalRequestException() throws Exception {
//        UserDTO userDTO = new UserDTO();
//        userDTO.setName("testUser ");
//        userDTO.setPassword("testPassword");
//
//        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDTO))));
//        doThrow(new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "User already exists"))
//                .when(usersService).registerUser (any(UserDTO.class), any(HttpServletRequest.class));
//
//        registerController.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        verify(writer).write("{\"message\": \"Registration failed (User already exists)\"}");
//    }
//
//    @Test
//    void testDoPost_JsonProcessingException() throws Exception {
//        when(request.getReader()).thenThrow(new IOException("Reader error"));
//
//        registerController.doPost(request, response);
//
//        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
//        verify(writer).write("{\"message\": \"Incorrect json (Reader error)\"}");
//    }
//}