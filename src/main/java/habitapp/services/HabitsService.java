package habitapp.services;

import habitapp.annotaions.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.UserDTO;
import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.enums.HabitFrequency;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.mappers.HabitMapper;
import habitapp.mappers.UserMapper;
import habitapp.repositories.CompletedDaysRepository;
import habitapp.repositories.HabitsRepository;
import habitapp.repositories.UsersRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с привычками.
 */
@Loggable
public class HabitsService implements UserMapper, HabitMapper {

    /**
     * Преобразует объект пользователя в объект UserDTO.
     *
     * @param user объект пользователя
     * @return объект UserDTO
     */
    @Override
    public UserDTO userToUserDTO(User user) {
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    /**
     * Преобразует объект UserDTO в объект пользователя.
     *
     * @param userDTO объект UserDTO
     * @return объект пользователя
     */
    @Override
    public User userDTOToUser (UserDTO userDTO) {
        return UserMapper.INSTANCE.userDTOToUser (userDTO);
    }

    /**
     * Преобразует объект Habit в объект HabitDTO.
     *
     * @param habit объект Habit
     * @return объект HabitDTO
     */
    @Override
    public HabitDTO habitToHabitDTO(Habit habit) {
        return HabitMapper.INSTANCE.habitToHabitDTO(habit);
    }

    /**
     * Преобразует объект HabitDTO в объект Habit.
     *
     * @param habitDTO объект HabitDTO
     * @return объект Habit
     */
    @Override
    public Habit habitDTOToHabit(HabitDTO habitDTO) {
        return HabitMapper.INSTANCE.habitDTOToHabit(habitDTO);
    }

    private static final HabitsService habitsService = new HabitsService();

    /**
     * Получает экземпляр сервиса привычек.
     *
     * @return экземпляр HabitsService
     */
    public static HabitsService getInstance() {
        return habitsService;
    }

    private HabitsService() {
        habitsRepository = HabitsRepository.getInstance();
        usersRepository = UsersRepository.getInstance();
        completedDaysRepository = CompletedDaysRepository.getInstance();
        logger = LoggerFactory.getLogger(HabitsService.class);
    }

    private final UsersRepository usersRepository;
    private final HabitsRepository habitsRepository;
    private final CompletedDaysRepository completedDaysRepository;
    private final Logger logger;

    /**
     * Получает все привычки пользователя.
     *
     * @param req HTTP-запрос
     * @return список HabitDTO
     * @throws UserIllegalRequestException если пользователь не авторизован
     */
    public List<HabitDTO> getAllHabits(HttpServletRequest req) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        unmarkHabitsIfTimeLeft(req);

        if (!usersRepository.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"Вы не авторизованы для получения ваших привычек!\"}");
        }

        int userId = usersRepository.getUserId(userDTOToUser (userDTO));
        return habitsRepository.getAllHabits(userId).stream()
                .map(this::habitToHabitDTO)
                .collect(Collectors.toList());
    }

    /**
     * Редактирует привычку.
     *
     * @param req      HTTP-запрос
     * @param habitDTOS массив HabitDTO, содержащий старую и новую привычки
     * @throws UserIllegalRequestException если пользователь не авторизован или привычка не найдена
     */
    public void redactHabit(HttpServletRequest req, HabitDTO[] habitDTOS) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        HabitDTO oldHabit = habitDTOS[0];
        HabitDTO newHabit = habitDTOS[1];

        if (!usersRepository.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"Вы не авторизованы для редактирования вашей привычки!\"}");
        } else if (!habitsRepository.hasHabit(habitDTOToHabit(oldHabit), usersRepository.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse .SC_NOT_FOUND, "{\"message\": \"Привычка, которую вы хотите редактировать, не найдена!\"}");
        }
        habitsRepository.redactHabit(habitDTOToHabit(oldHabit), habitDTOToHabit(newHabit), usersRepository.getUserId(userDTOToUser (userDTO)));
    }

    /**
     * Удаляет привычку.
     *
     * @param req     HTTP-запрос
     * @param habitDTO объект HabitDTO, содержащий удаляемую привычку
     * @throws UserIllegalRequestException если пользователь не авторизован или привычка не найдена
     */
    public void deleteHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!usersRepository.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"Вы не авторизованы для удаления вашей привычки!\"}");
        } else if (!(habitsRepository.hasHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Привычка, которую вы хотите удалить, не найдена!\"}");
        }
        habitsRepository.deleteHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser (userDTO)));
    }

    /**
     * Создает новую привычку.
     *
     * @param req     HTTP-запрос
     * @param habitDTO объект HabitDTO, содержащий создаваемую привычку
     * @throws UserIllegalRequestException если пользователь не авторизован или привычка уже существует
     */
    public void createHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!(usersRepository.hasUser (userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"Вы не авторизованы для создания новой привычки!\"}");
        } else if (habitsRepository.hasHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Привычка, которую вы хотите создать, уже существует!\"}");
        }
        int userId = usersRepository.getUserId(userDTOToUser (userDTO));
        habitsRepository.createHabit(habitDTOToHabit(habitDTO), userId);
    }

    /**
     * Помечает привычку как выполненную.
     *
     * @param req     HTTP-запрос
     * @param habitDTO объект HabitDTO, содержащий помечаемую привычку
     * @throws UserIllegalRequestException если пользователь не авторизован или привычка не найдена
     */
    public void markHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        unmarkHabitsIfTimeLeft(req);

        if (!usersRepository.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"Вы не авторизованы для пометки вашей привычки!\"}");
        } else if (!habitsRepository.hasHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Привычка, которую вы хотите пометить, не найдена!\"}");
        } else if (habitsRepository.isMarked(habitsRepository.getHabitId(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser  (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "{\"message\": \"Привычка, которую вы хотите пометить, уже помечена!\"}");
        }

        int userId = usersRepository.getUserId(userDTOToUser (userDTO));
        int habitId = habitsRepository.getHabitId(habitDTOToHabit(habitDTO), userId);

        habitsRepository.markHabit(habitDTOToHabit(habitDTO), userId);
        completedDaysRepository.createCompletedDay(habitId);
    }

    /**
     * Получает список дат, когда привычка была выполнена.
     *
     * @param req     HTTP-запрос
     * @param habitDTO объект HabitDTO, содержащий привычку
     * @return список дат
     * @throws UserIllegalRequestException если пользователь не авторизован или привычка не найдена
     */
    public List<LocalDateTime> getCompletedDays(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!usersRepository.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"Вы не авторизованы для просмотра статистики вашей привычки!\"}");
        } else if (!habitsRepository.hasHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Привычка, для которой вы хотите просмотреть статистику, не найдена!\"}");
        }
        int habitId = habitsRepository.getHabitId(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser (userDTO)));
        return completedDaysRepository.getCompletedDays(habitId);
    }

    /**
     * Снимает пометку с привычек, если время для выполнения прошло.
     *
     * @param req HTTP-запрос
     * @throws UserIllegalRequestException если пользователь не авторизован
     */
    public void unmarkHabitsIfTimeLeft(HttpServletRequest req) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        List<HabitDTO> habitDTOS = habitsRepository.getAllHabits(usersRepository.getUserId(userDTOToUser (userDTO)))
                .stream().map(this::habitToHabitDTO).toList();

        int userId = usersRepository.getUserId(userDTOToUser (userDTO));
        for (HabitDTO habitDTO : habitDTOS) {
            int habitId = habitsRepository.getHabitId(habitDTOToHabit(habitDTO), userId);
            HabitFrequency frequency = habitDTO.getFrequency(); // frequency  либо HabitFrequency.WEEKLY либо HabitFrequency.DAILY
            LocalDateTime lastCompletedDate = completedDaysRepository.getLastCompletedDay(habitId);
            if (lastCompletedDate != null) {
                LocalDateTime now = LocalDateTime.now();
                long daysBetween = java.time.Duration.between(lastCompletedDate, now).toDays();
                int daysInWeek = 7;
                int oneDay = 1;
                if ((frequency == HabitFrequency.DAILY && daysBetween > oneDay) ||
                        (frequency == HabitFrequency.WEEKLY && daysBetween > daysInWeek)) {
                    habitsRepository.unmarkHabit(habitId);
                    logger.debug("habit with id {} was unmarked", habitId);
                }
            } else {
                logger.debug("No completed days found for habit (id = {}). Skipping unmarking.", habitId);
            }
        }
    }
}