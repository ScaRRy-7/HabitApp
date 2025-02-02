package habitapp.services;

import habitapp.com.habitappauditloggerstarter.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.UserDTO;
import habitapp.enums.HabitFrequency;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.jwt.JwtUtil;
import habitapp.mappers.HabitMapper;
import habitapp.mappers.UserMapper;
import habitapp.repositories.CompletedDaysRepositoryImpl;
import habitapp.repositories.HabitsRepositoryImpl;
import habitapp.repositories.UsersRepositoryImpl;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с привычками.
 * Предоставляет методы для создания, редактирования, удаления и получения привычек пользователя.
 */
@RequiredArgsConstructor
@Service
@Slf4j
@Loggable
public class HabitsServiceImpl implements HabitsService {

    private final UsersRepositoryImpl usersRepositoryImpl;
    private final HabitsRepositoryImpl habitsRepositoryImpl;
    private final CompletedDaysRepositoryImpl completedDaysRepositoryImpl;
    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;
    private final HabitMapper habitMapper;

    /**
     * Получает все привычки пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @return Список привычек пользователя.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public List<HabitDTO> getAllHabits(String authHeader) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser (email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
        }
        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO));
        return habitsRepositoryImpl.getAllHabits(userId).stream()
                .map(habitMapper::habitToHabitDTO)
                .collect(Collectors.toList());
    }

    /**
     * Редактирует привычку пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @param habitDTOS Массив привычек, содержащий старую и новую привычку.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public void redactHabit(String authHeader, HabitDTO[] habitDTOS) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser (email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
        }
        HabitDTO oldHabit = habitDTOS[0];
        HabitDTO newHabit = habitDTOS[1];

        if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"Вы не авторизованы для редактирования своей привычки!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(oldHabit), usersRepositoryImpl.getUserId(userMapper.userDTOToUser(userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"Привычка, которую вы хотите отредактировать, не найдена!\"}");
        }
        habitsRepositoryImpl.editHabit(habitMapper.habitDTOToHabit(oldHabit), habitMapper.habitDTOToHabit(newHabit),
                usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)));
    }

    /**
     * Удаляет привычку пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @param habitDTO  Привычка для удаления.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public void deleteHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser (email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
        }

        if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"Вы не авторизованы для удаления своей привычки!\"}");
        } else if (!(habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"Привычка, которую вы хотите удалить, не найдена!\"}");
        }
        habitsRepositoryImpl.deleteHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)));
    }

    /**
     * Создает новую привычку для пользователя.
     *
     * @param authHeader Заголовок авторизации.
     * @param habitDTO  Привычка для создания.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public void createHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser (email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
        }

        if (!(usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"Вы не авторизованы для создания новой привычки!\"}");
        } else if (habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT,
                    "{\"message\": \"Привычка, которую вы хотите создать, уже существует!\"}");
        }
        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO));
        habitsRepositoryImpl.createHabit(habitMapper.habitDTOToHabit(habitDTO), userId);
    }

    /**
     * Помечает привычку как выполненную.
     *
     * @param authHeader Заголовок авторизации.
     * @param habitDTO  Привычка для пометки.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public void markHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser (email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
        }
        unmarkHabitsIfTimeLeft(authHeader);

        if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"Вы не авторизованы для пометки своей привычки!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"Привычка, которую вы хотите пометить, не найдена!\"}");
        } else if (habitsRepositoryImpl.isMarked(habitsRepositoryImpl.getHabitId(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT,
                    "{\"message\": \"Привычка, которую вы хотите пометить, уже помечена!\"}");
        }

        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO));
        int habitId = habitsRepositoryImpl.getHabitId(habitMapper.habitDTOToHabit(habitDTO), userId);

        habitsRepositoryImpl.markHabit(habitMapper.habitDTOToHabit(habitDTO), userId);
        completedDaysRepositoryImpl.createCompletedDay(habitId);
    }

    /**
     * Получает список выполненных дней для привычки.
     *
     * @param authHeader Заголовок авторизации.
     * @param habitDTO  Привычка для получения статистики.
     * @return Список выполненных дней.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public List<LocalDateTime> getCompletedDays(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser (email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
        } else if (habitDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Habit cannot be null");
        }

        if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"Вы не авторизованы для просмотра статистики своей привычки!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"Привычка, для которой вы хотите получить статистику, не найдена!\"}");
        }
        int habitId = habitsRepositoryImpl.getHabitId(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)));
        return completedDaysRepositoryImpl.getCompletedDays(habitId);
    }

    /**
     * Снимает отметку с привычек, если осталось время.
     *
     * @param authHeader Заголовок авторизации.
     * @throws UserIllegalRequestException В случае некорректного запроса.
     */
    @Override
    public void unmarkHabitsIfTimeLeft(String authHeader) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser (email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Пользователь не найден");
        }

        List<HabitDTO> habitDTOS = habitsRepositoryImpl.getAllHabits(usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))
                .stream().map(habitMapper::habitToHabitDTO).toList();

        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO));
        for (HabitDTO habitDTO : habitDTOS) {
            int habitId = habitsRepositoryImpl.getHabitId(habitMapper.habitDTOToHabit(habitDTO), userId);
            HabitFrequency frequency = habitDTO.getFrequency(); // frequency  либо HabitFrequency.WEEKLY либо HabitFrequency.DAILY
            LocalDateTime lastCompletedDate = completedDaysRepositoryImpl.getLastCompletedDay(habitId);
            if (lastCompletedDate != null) {
                LocalDateTime now = LocalDateTime.now();
                long daysBetween = java.time.Duration.between(lastCompletedDate, now).toDays();
                int daysInWeek = 7;
                int oneDay = 1;
                if ((frequency == HabitFrequency.DAILY && daysBetween > oneDay) ||
                        (frequency == HabitFrequency.WEEKLY && daysBetween > daysInWeek)) {
                    habitsRepositoryImpl.unmarkHabit(habitId);
                    log.debug("Привычка с id {} была снята с отметки", habitId);
                }
            } else {
                log.debug("Нет выполненных дней для привычки (id = {}). Пропускаем снятие отметки.", habitId);
            }
        }
    }

    /**
     * Получает частоту привычки по имени.
     *
     * @param name Имя частоты привычки.
     * @return Частота привычки.
     * @throws IllegalArgumentException Если частота не существует.
     */
    @Override
    public HabitFrequency getFrequencyByName(String name) {
        for (HabitFrequency frequency : HabitFrequency.values()) {
            if (frequency.getName().equals(name)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("Эта частота привычки не существует!: " + name);
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
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Нек орректный токен");
        }

        return email;
    }
}