package habitapp.controllers;

import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
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

class RegisterControllerTest {

    @InjectMocks
    private RegisterController registerController;

    @Mock
    private UsersService usersService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void register_Success() {
        UserDTO userDTO = new UserDTO();
        doNothing().when(usersService).registerUser(any(UserDTO.class), any(HttpServletRequest.class));

        ResponseEntity<String> response = registerController.register(request, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"User registered successfully\"}", response.getBody());
    }

    @Test
    void register_UserAlreadyExists() {
        UserDTO userDTO = new UserDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.CONFLICT.value(),
                "{\"message\": \"User already exists\"}"))
                .when(usersService).registerUser(any(UserDTO.class), any(HttpServletRequest.class));

        ResponseEntity<String> response = registerController.register(request, userDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"User already exists\"}", response.getBody());
    }

    @Test
    void register_InvalidJson() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        doThrow(new HttpMessageNotReadableException("Invalid JSON format"))
                .when(usersService).registerUser(any(UserDTO.class), any(HttpServletRequest.class));

        ResponseEntity<String> response = registerController.register(request, userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Incorrect json (Invalid JSON format)\"}", response.getBody());
    }

    @Test
    void register_NullUserDTO() {
        ResponseEntity<String> response = registerController.register(request, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"user cannot be null\"}", response.getBody());
    }

    @Test
    void register_InvalidUserData() {
        UserDTO userDTO = new UserDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.BAD_REQUEST.value(),
                "{\"message\": \"Invalid user data\"}"))
                .when(usersService).registerUser(any(UserDTO.class), any(HttpServletRequest.class));

        ResponseEntity<String> response = registerController.register(request, userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Invalid user data\"}", response.getBody());
    }
}