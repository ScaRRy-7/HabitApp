package habitapp.services;

import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dto.UserDTO;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.jwt.JwtUtil;
import habitapp.mappers.UserMapper;
import habitapp.repositories.UsersRepositoryImpl;
import habitapp.validators.UserValidator;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет методы для регистрации, входа, редактирования и удаления учетных записей пользователей.
 */
@Service
@RequiredArgsConstructor
@Loggable
public class UsersServiceImpl implements UsersService {

    private final UserValidator userValidator;
    private final UsersRepositoryImpl usersRepositoryImpl;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    /**
     * Регистрирует нового пользователя.
     *
     * @param userDTO Данные пользователя для регистрации.
     * @return JWT токен для нового пользователя.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public String registerUser (UserDTO userDTO) throws UserIllegalRequestException {
        if (!userValidator.validUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Некорректные данные пользователя");
        } else if (usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "Пользователь уже существует");
        }
        usersRepositoryImpl.addUser (userMapper.userDTOToUser (userDTO));
        return jwtUtil.generateToken(userDTO.getEmail());
    }

    /**
     * Выполняет вход пользователя.
     *
     * @param userDTO Данные пользователя для входа.
     * @return JWT токен для пользователя.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public String loginUser (UserDTO userDTO) throws UserIllegalRequestException {
        if (!userValidator.validUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Некорректные данные пользователя");
        } else if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "Пользователь не существует");
        } else if (!usersRepositoryImpl.getUser (userDTO.getEmail()).getPassword().equals(userDTO.getPassword())) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "Некорректный пароль");
        }
        // Генерация JWT токена
        return jwtUtil.generateToken(userDTO.getEmail());
    }

    /**
     * Редактирует данные пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @param userDTO   Новые данные пользователя.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public void redactUser (String authHeader, UserDTO userDTO) throws UserIllegalRequestException {
        if (!userValidator.validUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Некорректный JSON запрос");
        }
        String token = authHeader.substring(7);
        String email = validateTokenAndGetEmail(token);
        User oldUser  = usersRepositoryImpl.getUser (email);
        User newUser  = userMapper.userDTOToUser (userDTO);
        usersRepositoryImpl.redactUser (oldUser , newUser );
    }

    /**
     * Удаляет учетную запись пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public void removeAccount(String authHeader) throws UserIllegalRequestException {
        String token = authHeader.substring(7);
        String email = validateTokenAndGetEmail(token);
        usersRepositoryImpl.deleteUser (usersRepositoryImpl.getUser (email));
    }

    /**
     * Проверяет токен и получает email пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @return Email пользователя.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    public String validateTokenAndGetEmail(String authHeader) throws UserIllegalRequestException {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Некорректный заголовок авторизации");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractEmail(token);

        if (email == null || !jwtUtil.validateToken(token, email)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Некорректный токен");
        }

        return email;
    }
}