package habitapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AccountControllerTest {

    private AccountController accountController;
    private UsersService usersService;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() throws IOException  {
        usersService = mock(UsersService.class);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        objectMapper = new ObjectMapper();

        accountController = new AccountController();
        accountController.setObjectMapper(objectMapper);
        accountController.setUsersService(usersService);

        PrintWriter writer = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(writer);
    }



    @Test
    public void testDoPut_Success() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password");

        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDTO))));
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        accountController.doPut(request, response);
        ArgumentCaptor<UserDTO> userDTOCaptor = ArgumentCaptor.forClass(UserDTO.class);
        verify(usersService).redactUser (eq(request), userDTOCaptor.capture());

        UserDTO capturedUserDTO = userDTOCaptor.getValue();
        Assertions.assertEquals(userDTO.getEmail(), capturedUserDTO.getEmail());
        Assertions.assertEquals(userDTO.getPassword(), capturedUserDTO.getPassword());

        verify(response).setStatus(HttpServletResponse.SC_OK);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        verify(response.getWriter()).write(captor.capture());
        Assertions.assertTrue(captor.getValue().contains("Account was redacted:"));
    }

    @Test
    public void testDoPut_InvalidJson() throws Exception {
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("invalid json")));
        accountController.doPut(request, response);
        verify(response).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        verify(response.getWriter()).write(contains("Incorrect json"));
    }

    @Test
    public void testDoPut_UserIllegalRequestException() throws Exception {
        UserDTO userDTO = new UserDTO();
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader(objectMapper.writeValueAsString(userDTO))));

        doThrow(new UserIllegalRequestException(HttpServletResponse.SC_FORBIDDEN, "User not authorized"))
                .when(usersService).redactUser (any(), any());

        accountController.doPut(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("User not authorized");
    }

    @Test
    public void testDoDelete_Success() throws Exception {
        when(request.getSession()).thenReturn(mock(HttpSession.class));

        accountController.doDelete(request, response);

        verify(usersService).removeAccount(request);
        verify(response).setStatus(HttpServletResponse.SC_OK);
        verify(response.getWriter()).write("{\"message\": \"Account removed\"}");
    }

    @Test
    public void testDoDelete_UserIllegalRequestException() throws Exception {
        doThrow(new UserIllegalRequestException(HttpServletResponse.SC_FORBIDDEN, "User not authorized"))
                .when(usersService).removeAccount (any());

        accountController.doDelete(request, response);

        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response.getWriter()).write("User not authorized");
    }
}