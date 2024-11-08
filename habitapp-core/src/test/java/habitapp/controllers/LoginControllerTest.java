package habitapp.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.MessageDTO;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock
    private UsersServiceImpl usersServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private LoginController loginController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Успешная аутентификация пользователя")
    public void testLogin_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        String token = "some-token";
        when(usersServiceImpl.loginUser (userDTO)).thenReturn(token);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"User   logged in successfully, token: " + token + "\"}");

        ResponseEntity<String> response = loginController.login(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"User   logged in successfully, token: " + token + "\"}", response.getBody());
        verify(usersServiceImpl).loginUser (userDTO);
    }

    @Test
    @DisplayName("Ошибка 401 при неправильных данных аутентификации")
    public void testLogin_UserIllegalRequestException() throws Exception {
        UserDTO userDTO = new UserDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "Invalid credentials"))
                .when(usersServiceImpl).loginUser (userDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"Invalid credentials\"}");

        ResponseEntity<String> response = loginController.login(userDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\": \"Invalid credentials\"}", response.getBody());
    }

    @Test
    @DisplayName("Ошибка 400 при некорректном JSON")
    public void testLogin_BadRequest() throws Exception {
        UserDTO userDTO = new UserDTO();
        doThrow(new HttpMessageNotReadableException("Malformed JSON"))
                .when(usersServiceImpl).loginUser (userDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"Incorrect json (Malformed JSON)\"}");

        ResponseEntity<String> response = loginController.login(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Incorrect json (Malformed JSON)\"}", response.getBody());
    }
}