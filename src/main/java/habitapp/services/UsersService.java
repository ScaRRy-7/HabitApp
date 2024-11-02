package habitapp.services;

import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.mappers.UserMapper;
import habitapp.repositories.UsersRepository;
import habitapp.validators.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления пользователями.
 * <p>
 * Этот класс предоставляет методы для регистрации, входа в систему, редактирования
 * и удаления пользователей. Он управляет проверкой данных пользователя и
 * взаимодействием с репозиторием пользователей.
 * </p>
 */
@Loggable
@Service
public class UsersService implements UserMapper {



    private final UserValidator userValidator;
    private final UsersRepository usersRepository;

    /**
     * Конструктор для инициализации сервиса пользователей.
     */
    public UsersService(UserValidator userValidator, UsersRepository usersRepository) {
        this.userValidator = userValidator;
        this.usersRepository = usersRepository;
    }

    @Override
    public UserDTO userToUserDTO(User user) {
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        return UserMapper.INSTANCE.userDTOToUser(userDTO);
    }

    /**
     * Регистрация нового пользователя.
     *
     * @param userDTO Объект с данными пользователя для регистрации.
     * @param req     HTTP-запрос, используемый для взаимодействия с сессией.
     * @throws UserIllegalRequestException при некорректных данных пользователя или при наличии пользователя с такой же электронной почтой.
     */
    public void registerUser(UserDTO userDTO, HttpServletRequest req) throws UserIllegalRequestException {
        if (!userValidator.validateUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "invalid user data");
        } else if (usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "user already exists");
        }
        usersRepository.addUser(userDTOToUser(userDTO));
        req.getSession().setAttribute("user", userDTO);
    }

    /**
     * Авторизация пользователя.
     *
     * @param userDTO Объект с данными пользователя для входа.
     * @param req     HTTP-запрос, используемый для взаимодействия с сессией.
     * @throws UserIllegalRequestException при некорректных данных пользователя, если пользователь не существует или неправильный пароль.
     */
    public void loginUser(UserDTO userDTO, HttpServletRequest req) throws UserIllegalRequestException {
        if (!userValidator.validateUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "incorrect user data");
        } else if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "user not found");
        }

        UserDTO checkedUserDTO = userToUserDTO(usersRepository.getUser(userDTO.getEmail()));
        if (!(checkedUserDTO.getPassword().equals(userDTO.getPassword()))) {

            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "incorrect password");
        } else {
            req.getSession(true).setAttribute("user", checkedUserDTO);
        }
    }

    /**
     * Редактирование данных пользователя.
     *
     * @param req     HTTP-запрос, используемый для взаимодействия с сессией.
     * @param userDTO Объект с новыми данными пользователя.
     * @throws UserIllegalRequestException если пользователь не авторизован.
     */
    public void redactUser(HttpServletRequest req, UserDTO userDTO) throws UserIllegalRequestException {
        if (req.getSession().getAttribute("user") == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User is not logged in");
        }
        User oldUser = userDTOToUser((UserDTO) req.getSession().getAttribute("user"));
        User newUser = userDTOToUser(userDTO);
        usersRepository.redactUser(oldUser, newUser);
        req.getSession().setAttribute("user", userDTO);
    }

    /**
     * Удаление аккаунта пользователя.
     *
     * @param req HTTP-запрос, используемый для взаимодействия с сессией.
     * @throws UserIllegalRequestException если пользователь не авторизован.
     */
    public void removeAccount(HttpServletRequest req) throws UserIllegalRequestException {
        if (req.getSession().getAttribute("user") == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "user is not logged in");
        }
        User userToDelete = userDTOToUser((UserDTO) req.getSession().getAttribute("user"));
        usersRepository.deleteUser(userToDelete);
        req.getSession().removeAttribute("user");
    }
}
