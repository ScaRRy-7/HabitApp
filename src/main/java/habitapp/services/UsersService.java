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

/**
 * Сервис для управления пользователями.
 * <p>
 * Этот класс предоставляет методы для регистрации, входа в систему, редактирования
 * и удаления пользователей. Он управляет проверкой данных пользователя и
 * взаимодействием с репозиторием пользователей.
 * </p>
 */
@Loggable
public class UsersService implements UserMapper {

    /**
     * Экземпляр сервиса пользователей.
     */
    private static final UsersService usersService = new UsersService();

    /**
     * Получает экземпляр сервиса пользователей.
     *
     * @return Экземпляр `UsersService`.
     */
    public static UsersService getInstance() {
        return usersService;
    }

    private final UserValidator userValidator;
    private final UsersRepository usersRepository;

    /**
     * Конструктор для инициализации сервиса пользователей.
     */
    private UsersService() {
        userValidator = UserValidator.getInstance();
        usersRepository = UsersRepository.getInstance();
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
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Некорректные электронная почта, пароль или имя");
        } else if (usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "Пользователь уже существует");
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
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Некорректные электронная почта, пароль или имя");
        } else if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "Пользователь не существует");
        }

        UserDTO checkedUserDTO = userToUserDTO(usersRepository.getUser(userDTO.getEmail()));
        if (!(checkedUserDTO.getPassword().equals(userDTO.getPassword()))) {

            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Неправильный пароль");
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
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
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
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не авторизован");
        }
        User userToDelete = userDTOToUser((UserDTO) req.getSession().getAttribute("user"));
        usersRepository.deleteUser(userToDelete);
        req.getSession().removeAttribute("user");
    }
}
