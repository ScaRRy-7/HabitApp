package habitapp.controllers;

import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Loggable
@RestController
@RequestMapping("/account")
public class AccountController {

    private UsersService usersService;

    @Autowired
    public AccountController(UsersService usersService) {
        this.usersService = usersService;
    }

    @PutMapping
    public ResponseEntity<String> editAccount(HttpServletRequest req, @RequestBody UserDTO userDTO) {
        try {
            usersService.redactUser(req, userDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"message\": \"Account was redacted\"}");
        } catch (UserIllegalRequestException e) {
           return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteAccount(HttpServletRequest req) {
        try {
            usersService.removeAccount(req);
            return ResponseEntity.ok("{\"message\": \"Account was deleted\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        }
    }
}
