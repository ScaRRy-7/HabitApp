package habitapp.services;

import habitapp.dto.UserDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface UsersService {

    public String registerUser(UserDTO userDTO);
    public void redactUser(String authHeader, UserDTO userDTO);
    public void removeAccount(String authHeader);
    public String loginUser(UserDTO userDTO);
}
