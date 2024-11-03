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

class LoginControllerTest {

    @InjectMocks
    private LoginController loginController;

    @Mock
    private UsersService usersService;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_Success() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testUser");
        userDTO.setPassword("testPassword");

        // Act
        ResponseEntity<String> response = loginController.login(request, userDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"authorized\"}", response.getBody());
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setName("nonexistentUser");
        userDTO.setPassword("testPassword");

        doThrow(new UserIllegalRequestException(HttpStatus.NOT_FOUND.value(),
                "{\"message\": \"User not found\"}"))
                .when(usersService).loginUser(any(UserDTO.class), any(HttpServletRequest.class));

        // Act
        ResponseEntity<String> response = loginController.login(request, userDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"User not found\"}", response.getBody());
    }

    @Test
    void login_InvalidPassword() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        userDTO.setName("testUser");
        userDTO.setPassword("wrongPassword");

        doThrow(new UserIllegalRequestException(HttpStatus.CONFLICT.value(),
                "{\"message\": \"Invalid password\"}"))
                .when(usersService).loginUser(any(UserDTO.class), any(HttpServletRequest.class));

        // Act
        ResponseEntity<String> response = loginController.login(request, userDTO);

        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Invalid password\"}", response.getBody());
    }

    @Test
    void login_InvalidJsonFormat() {
        // Arrange
        UserDTO userDTO = new UserDTO();
        doThrow(new HttpMessageNotReadableException("Invalid JSON format"))
                .when(usersService).loginUser(any(UserDTO.class), any(HttpServletRequest.class));

        // Act
        ResponseEntity<String> response = loginController.login(request, userDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Incorrect json (Invalid JSON format)\"}", response.getBody());
    }

    @Test
    void login_NullUserDTO() {
        // Act
        ResponseEntity<String> response = loginController.login(request, null);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"user cannot be null\"}", response.getBody());
    }

    @Test
    void login_EmptyCredentials() {
        // Arrange
        UserDTO userDTO = new UserDTO(); // empty credentials

        doThrow(new UserIllegalRequestException(HttpStatus.BAD_REQUEST.value(),
                "{\"message\": \"Username and password cannot be empty\"}"))
                .when(usersService).loginUser(any(UserDTO.class), any(HttpServletRequest.class));

        // Act
        ResponseEntity<String> response = loginController.login(request, userDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_JSON, response.getHeaders().getContentType());
        assertEquals("{\"message\": \"Username and password cannot be empty\"}", response.getBody());
    }
}