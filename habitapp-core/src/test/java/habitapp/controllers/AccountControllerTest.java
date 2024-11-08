package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.controllers.AccountController;
import habitapp.dto.MessageDTO;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.jwt.JwtUtil;
import habitapp.services.UsersServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    @Mock
    private UsersServiceImpl usersServiceImpl;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AccountController accountController;

    private UserDTO userDTO;
    private String authHeader;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO(); //
        userDTO.setEmail("user@example.com");
        userDTO.setPassword("password");

        // Генерация токена
        String token = jwtUtil.generateToken(userDTO.getEmail());
        authHeader = "Bearer " + token;
    }

    @Test
    @DisplayName("Успешное редактирование данных аккаунта")
    public void testEditAccount_Success() throws Exception {
        doNothing().when(usersServiceImpl).redactUser (authHeader, userDTO);
        when(objectMapper.writeValueAsString(any(MessageDTO.class))).thenReturn("{\"message\": \"Аккаунт был изменён\"}");

        ResponseEntity<String> response = accountController.editAccount(authHeader, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Аккаунт был изменён\"}", response.getBody());
        verify(usersServiceImpl).redactUser (authHeader, userDTO);
    }

    @Test
    @DisplayName("Обработка UserIllegalRequestException при редактировании аккаунта")
    public void testEditAccount_UserIllegalRequestException() throws Exception {
        doThrow(new UserIllegalRequestException(HttpStatus.BAD_REQUEST.value(), "Некорректный JSON запрос"))
                .when(usersServiceImpl).redactUser (authHeader, userDTO);
        when(objectMapper.writeValueAsString(any(MessageDTO.class))).thenReturn("{\"message\": \"Некорректный JSON запрос\"}");

        ResponseEntity<String> response = accountController.editAccount(authHeader, userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"Некорректный JSON запрос\"}", response.getBody());
    }

    @Test
    @DisplayName("Успешное удаление аккаунта")
    public void testDeleteAccount_Success() throws Exception {
        doNothing().when(usersServiceImpl).removeAccount(authHeader);
        when(objectMapper.writeValueAsString(any(MessageDTO.class))).thenReturn("{\"message\": \"Аккаунт был удалён\"}");

        ResponseEntity<String> response = accountController.deleteAccount(authHeader);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Аккаунт был удалён\"}", response.getBody());
        verify(usersServiceImpl).removeAccount(authHeader);
    }

    @Test
    @DisplayName("Обработка UserIllegalRequestException при удалении аккаунта")
    public void testDeleteAccount_UserIllegalRequestException() throws Exception {
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "Некорректный токен"))
                .when(usersServiceImpl).removeAccount(authHeader);
        when(objectMapper.writeValueAsString(any(MessageDTO.class))).thenReturn("{\"message\": \"Некорректный токен\"}");

        ResponseEntity<String> response = accountController.deleteAccount(authHeader);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("{\"message\": \"Некорректный токен\"}", response.getBody());
    }
}