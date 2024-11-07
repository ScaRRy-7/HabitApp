package habitapp.services;

import habitapp.annotations.Loggable;
import habitapp.dto.UserDTO;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.jwt.JwtUtil;
import habitapp.mappers.UserMapper;
import habitapp.repositories.UsersRepository;
import habitapp.repositories.UsersRepositoryImpl;
import habitapp.validators.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Сервис для управления пользователями.
 * <p>
 * Этот класс предоставляет методы для регистрации, входа в систему, редактирования
 * и удаления пользователей. Он управляет проверкой данных пользователя и
 * взаимодействием с репозиторием пользователей.
 * </p>
 */
@Service
public class UsersServiceImpl implements UsersService {

    private final UserValidator userValidator;
    private final UsersRepositoryImpl usersRepositoryImpl;
    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;
    
    public UsersServiceImpl(UserValidator userValidator, UsersRepositoryImpl usersRepositoryImpl, JwtUtil jwtUtil, UserMapper userMapper) {
        this.userValidator = userValidator;
        this.usersRepositoryImpl = usersRepositoryImpl;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }
    

    @Override
    public String registerUser(UserDTO userDTO) throws UserIllegalRequestException {
        if (!userValidator.validUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "invalid user data");
        } else if (usersRepositoryImpl.hasUser(userMapper.userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "user already exists");
        }
        usersRepositoryImpl.addUser(userMapper.userDTOToUser(userDTO));
        return jwtUtil.generateToken(userDTO.getEmail());
    }

    @Override
    public String loginUser(UserDTO userDTO) throws UserIllegalRequestException {
        if (!userValidator.validUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "invalid user data");
        } else if (!usersRepositoryImpl.hasUser(userMapper.userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "user does not exist");
        } else if (!usersRepositoryImpl.getUser(userDTO.getEmail()).getPassword().equals(userDTO.getPassword())) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "invalid password");
        }
        // генерация JWT токена
        return jwtUtil.generateToken(userDTO.getEmail());

    }

    @Override
    public void redactUser(String authHeader, UserDTO userDTO) throws UserIllegalRequestException {
    if (!userValidator.validUserData(userDTO)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Incorrect json request");
    }
    String token = authHeader.substring(7);
    String email = validateTokenAndGetEmail(token);
    User oldUser = usersRepositoryImpl.getUser(email);
    User newUser = userMapper.userDTOToUser(userDTO);
    usersRepositoryImpl.redactUser(oldUser, newUser);
    }


    @Override
    public void removeAccount(String authHeader) throws UserIllegalRequestException {
        String token = authHeader.substring(7);
        String email = validateTokenAndGetEmail(token);
        usersRepositoryImpl.deleteUser(usersRepositoryImpl.getUser(email));

    }

    public String validateTokenAndGetEmail(String authHeader) throws UserIllegalRequestException {
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid Authorization header");
        }

        String token = authHeader.substring(7);
        String email = jwtUtil.extractUsername(token);

        if (email == null || !jwtUtil.validateToken(token, email)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }

        return email;
    }

}
