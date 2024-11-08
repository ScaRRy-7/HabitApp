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

class RegisterControllerTest {

    @Mock
    private UsersServiceImpl usersServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Успешная регистрация пользователя")
    public void testRegister_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        String token = "some-token";
        when(usersServiceImpl.registerUser (userDTO)).thenReturn(token);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"User   registered successfully, token: " + token + "\"}");

        ResponseEntity<String> response = registerController.register(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"User   registered successfully, token: " + token + "\"}", response.getBody());
        verify(usersServiceImpl).registerUser (userDTO);
    }

    @Test
    @DisplayName("Ошибка 400 при некорректном JSON")
    public void testRegister_BadRequest() throws Exception {
        UserDTO userDTO = new UserDTO();
        doThrow(new HttpMessageNotReadableException("Malformed JSON"))
                .when(usersServiceImpl).registerUser (userDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"Incorrect json (Malformed JSON)\"}");

        ResponseEntity<String> response = registerController.register(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Incorrect json (Malformed JSON)\"}", response.getBody());
    }

    @Test
    @DisplayName("Ошибка 409 при попытке зарегистрировать существующего пользователя")
    public void testRegister_UserAlreadyExists() throws Exception {
        UserDTO userDTO = new UserDTO();
        doThrow(new UserIllegalRequestException(HttpStatus.CONFLICT.value(), "User   already exists"))
                .when(usersServiceImpl).registerUser (userDTO);
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"message\": \"User   already exists\"}");

        ResponseEntity<String> response = registerController.register(userDTO);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("{\"message\": \"User   already exists\"}", response.getBody());
    }
}