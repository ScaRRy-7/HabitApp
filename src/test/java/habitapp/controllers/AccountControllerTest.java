package habitapp.controllers;

import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

public class AccountControllerTest {

    @InjectMocks
    private AccountController accountController;

    @Mock
    private UsersService usersService;

    @Mock
    private HttpServletRequest req;

    private UserDTO userDTO;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");
    }

    @Test
    public void testEditAccount_Success() {
        ResponseEntity<String> response = accountController.editAccount(req, userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Account was redacted\"}", response.getBody());

        verify(usersService).redactUser (req, userDTO);
    }

    @Test
    public void testEditAccount_UserNotLoggedIn() {
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "User  is not logged in"))
                .when(usersService).redactUser (any(), any());

        ResponseEntity<String> response = accountController.editAccount(req, userDTO);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User  is not logged in", response.getBody());
    }

    @Test
    public void testEditAccount_InvalidJson() {
        ResponseEntity<String> response = accountController.editAccount(req, null);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("{\"message\": \"user cannot be null\"}", response.getBody());
    }

    @Test
    public void testDeleteAccount_Success() {
        ResponseEntity<String> response = accountController.deleteAccount(req);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("{\"message\": \"Account was deleted\"}", response.getBody());

        verify(usersService).removeAccount(req);
    }

    @Test
    public void testDeleteAccount_UserNotLoggedIn() {
        doThrow(new UserIllegalRequestException(HttpStatus.UNAUTHORIZED.value(), "user is not logged in"))
                .when(usersService).removeAccount(any());

        ResponseEntity<String> response = accountController.deleteAccount(req);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("user is not logged in", response.getBody());
    }
}