package habitapp.services;

import habitapp.annotaions.Loggable;
import habitapp.dto.UserDTO;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.mappers.UserMapper;
import habitapp.repositories.UsersRepository;
import habitapp.validators.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Loggable
public class UsersService implements UserMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        return UserMapper.INSTANCE.userDTOToUser(userDTO);
    }

    private static final UsersService usersService = new UsersService();
    public static UsersService getInstance() {
        return usersService;
    }
    private UsersService() {
        userValidator = UserValidator.getInstance();
        usersRepository = UsersRepository.getInstance();
    }
    private final UserValidator userValidator;
    private final UsersRepository usersRepository;

    public void registerUser(UserDTO userDTO, HttpServletRequest req) throws UserIllegalRequestException {
        if (!userValidator.validateUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or password or name");
        } else if (usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "User already exists");
        }

        usersRepository.addUser(userDTOToUser(userDTO));
        req.getSession().setAttribute("user", userDTO);
    }

    public void loginUser(UserDTO userDTO, HttpServletRequest req) throws UserIllegalRequestException {
        if (!userValidator.validateUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or password or name");
        } else if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "User does not exist");
        }

        UserDTO checkedUserDTO = userToUserDTO(usersRepository.getUser(userDTO.getEmail()));
        if (!(checkedUserDTO.getPassword().equals(userDTO.getPassword()))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Incorrect password");
        } else {
            req.getSession(true).setAttribute("user", checkedUserDTO);
        }
    }

    public void redactUser(HttpServletRequest req, UserDTO userDTO) throws UserIllegalRequestException {
        if (req.getSession().getAttribute("user") == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not unauthorized");
        }
        User oldUser = userDTOToUser((UserDTO) req.getSession().getAttribute("user"));
        User newUser = userDTOToUser(userDTO);
        usersRepository.redactUser(oldUser, newUser);
        req.getSession().setAttribute("user", userDTO);
    }

    public void removeAccount(HttpServletRequest req) throws UserIllegalRequestException {
        if (req.getSession().getAttribute("user") == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not unauthorized");
        }
        User userToDelete = userDTOToUser((UserDTO) req.getSession().getAttribute("user"));
        usersRepository.deleteUser(userToDelete);
        req.getSession().removeAttribute("user");
    }
}
