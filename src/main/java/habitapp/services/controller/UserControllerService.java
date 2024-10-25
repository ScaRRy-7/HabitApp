package habitapp.services.controller;

import habitapp.annotaions.Loggable;
import habitapp.dto.UserDTO;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.mappers.UserMapper;
import habitapp.validators.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Loggable
public class UserControllerService implements UserMapper {

    private static final UserControllerService INSTANCE = new UserControllerService();

    private final UserValidator userValidator;
    private final HabitappRepository habitappRepository;

    private UserControllerService() {
        this.userValidator = UserValidator.INSTANCE;
        this.habitappRepository = new HabitappRepository();
    }

    public static UserControllerService getInstance() {
        return INSTANCE;
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        return UserMapper.INSTANCE.userDTOToUser(userDTO);
    }

    public void registerUser(UserDTO userDTO, HttpServletRequest req) throws UserIllegalRequestException {
        if (!userValidator.validateUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or password or name");
        } else if (habitappRepository.hasUser(userDTO.getEmail())) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "User already exists");
        }

        habitappRepository.addUserToDatabase(new User(userDTO.getName(), userDTO.getEmail(), userDTO.getPassword()));
        req.getSession().setAttribute("user", userDTO);
    }

    public void loginUser(UserDTO userDTO, HttpServletRequest req) throws UserIllegalRequestException {
        if (!userValidator.validateUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Invalid email or password or name");
        }

        UserDTO checkedUserDTO = userToUserDTO(habitappRepository.getUserFromDatabase(userDTO.getEmail()));
        if (!(userValidator.userExists(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "User not found");
        } else if (!(checkedUserDTO.getPassword().equals(userDTO.getPassword()))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Incorrect password");
        }

        req.getSession(true).setAttribute("user", checkedUserDTO);
    }

    public void redactUser(HttpServletRequest req, UserDTO userDTO) throws UserIllegalRequestException {
        if (req.getSession().getAttribute("user") == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not unauthorized");
        }

        habitappRepository.updateRedactedUser(((UserDTO) req.getSession().getAttribute("user")).getEmail(),
                userDTOToUser(userDTO));
        req.getSession().setAttribute("user", userDTO);
    }

    public void removeAccount(HttpServletRequest req) throws UserIllegalRequestException {
        if (req.getSession().getAttribute("user") == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not unauthorized");
        }

        habitappRepository.removeUserFromDatabase(( (UserDTO) req.getSession().getAttribute("user")).getEmail());
        req.getSession().removeAttribute("user");
    }
}
