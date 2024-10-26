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

@Loggable
public class HabitsService implements UserMapper, HabitMapper {

    @Override
    public UserDTO userToUserDTO(User user) {
        return UserMapper.INSTANCE.userToUserDTO(user);
    }

    @Override
    public User userDTOToUser(UserDTO userDTO) {
        return UserMapper.INSTANCE.userDTOToUser(userDTO);
    }

    @Override
    public HabitDTO habitToHabitDTO(Habit habit) {
        return HabitMapper.INSTANCE.habitToHabitDTO(habit);
    }

    @Override
    public Habit habitDTOToHabit(HabitDTO habitDTO) {
        return HabitMapper.INSTANCE.habitDTOToHabit(habitDTO);
    }

    private static final HabitsService habitsService = new HabitsService();
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

    public List<HabitDTO> getAllHabits(HttpServletRequest req) throws UserIllegalRequestException {
        UserDTO userDTO =  (UserDTO) req.getSession().getAttribute("user");
        unmarkHabitsIfTimeLeft(req);

        if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to get your habits!\"}");
        }

        int userId = usersRepository.getUserId(userDTOToUser(userDTO));
        return habitsRepository.getAllHabits(userId).stream()
                .map(this::habitToHabitDTO)
                .collect(Collectors.toList());
    }

    public void redactHabit(HttpServletRequest req, HabitDTO[] habitDTOS) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        HabitDTO oldHabit = habitDTOS[0];
        HabitDTO newHabit = habitDTOS[1];

        if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to redact your habit!\"}");
        } else if (!habitsRepository.hasHabit(habitDTOToHabit(oldHabit), usersRepository.getUserId(userDTOToUser(userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to redact -  not found!\"}");
        }
        habitsRepository.redactHabit(habitDTOToHabit(oldHabit), habitDTOToHabit(newHabit), usersRepository.getUserId(userDTOToUser(userDTO)) );
    }

    public void deleteHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to remove your habit!\"}");
        } else if (!(habitsRepository.hasHabit( habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser(userDTO)) ))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to remove -  not found!\"}");
        }
        habitsRepository.deleteHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser(userDTO)));
    }

    public void createHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!(usersRepository.hasUser(userDTOToUser(userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to remove your habit!\"}");
        } else if (habitsRepository.hasHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser(userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to create -  already exists!\"}");
        }
        int userId = usersRepository.getUserId(userDTOToUser(userDTO));
        habitsRepository.createHabit(habitDTOToHabit(habitDTO), userId);
    }

    public void markHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        unmarkHabitsIfTimeLeft(req);

        if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": " +
                    "\"You are not logged in to remove your habit!\"}");
        } else if (!habitsRepository.hasHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser(userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": " +
                    "\"Habit that you want to mark - does not exist!\"}");
        } else if (habitsRepository.isMarked(habitsRepository.getHabitId(habitDTOToHabit(habitDTO),
                usersRepository.getUserId(userDTOToUser(userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "{\"message\": " +
                    "\"Habit that you want to mark - already marked!\"}");
        }

        int userId = usersRepository.getUserId(userDTOToUser(userDTO));
        int habitId = habitsRepository.getHabitId(habitDTOToHabit(habitDTO), userId);

        habitsRepository.markHabit(habitDTOToHabit(habitDTO), userId);
        completedDaysRepository.createCompletedDay(habitId);
    }

    public List<LocalDateTime> getCompletedDays(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!usersRepository.hasUser(userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to see habit statistics!\"}");
        } else if (!habitsRepository.hasHabit(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser(userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to see for statistics -  not found!\"}");
        }
        int habitId = habitsRepository.getHabitId(habitDTOToHabit(habitDTO), usersRepository.getUserId(userDTOToUser(userDTO)));
        return completedDaysRepository.getCompletedDays(habitId);
    }

    public void unmarkHabitsIfTimeLeft(HttpServletRequest req) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        List<HabitDTO> habitDTOS = habitsRepository.getAllHabits(usersRepository.getUserId(userDTOToUser(userDTO)))
                .stream().map(this::habitToHabitDTO).toList();

        int userId = usersRepository.getUserId(userDTOToUser(userDTO));
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
