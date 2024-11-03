package habitapp.controllers;

import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.services.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Account Management", description = "API для управления аккаунтом")
@Loggable
@RestController
@RequestMapping("/account")
public class AccountController {

    private UsersService usersService;

    @Autowired
    public AccountController(UsersService usersService) {
        this.usersService = usersService;
    }

    @Operation(
            summary = "Edit account details",
            description = "Redacts the account info"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully redacted"),
            @ApiResponse(responseCode = "401", description = "User is not unauthorized"),
            @ApiResponse(responseCode = "400", description = "Invalid JSON")
    })
    @PutMapping
    public ResponseEntity<String> editAccount(HttpServletRequest req, @RequestBody UserDTO userDTO) {
        if (userDTO == null) {
            return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON)
                    .body("{\"message\": \"user cannot be null\"}");
        }

        try {
            usersService.redactUser(req, userDTO);
            return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body("{\"message\": \"Account was redacted\"}");
        } catch (UserIllegalRequestException e) {
            return ResponseEntity.status(e.getErrorCode()).body(e.getMessage());
        } catch (HttpMessageNotReadableException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(
            summary = "Delete account",
            description = "Delete account which user logged in"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Account successfully deleted"),
            @ApiResponse(responseCode = "401", description = "User is not unauthorized")
    })
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