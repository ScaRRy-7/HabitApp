package habitapp.services;

import habitapp.annotaions.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.UserDTO;
import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.mappers.HabitMapper;
import habitapp.mappers.UserMapper;
import habitapp.repositories.HabitappRepository;
import habitapp.validators.UserValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Loggable
public class HabitService implements UserMapper, HabitMapper {

    private final HabitappRepository habitappRepository;
    private final UserValidator userValidator;

    private static final HabitService INSTANCE = new HabitService();

    public static HabitService getInstance() {
        return INSTANCE;
    }

    private HabitService() {
        this.habitappRepository = new HabitappRepository();
        this.userValidator = UserValidator.INSTANCE;
    }

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

    public List<HabitDTO> getAllHabits(HttpServletRequest req) throws UserIllegalRequestException {
        UserDTO userDTO =  (UserDTO) req.getSession().getAttribute("user");
        if (!(userValidator.userExists(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to get your habits!\"}");
        }

        return habitappRepository.getAllHabits(userDTOToUser(userDTO)).stream()
                .map(this::habitToHabitDTO)
                .collect(Collectors.toList());
    }

    public void redactHabit(HttpServletRequest req, HabitDTO[] habitDTOS) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
        HabitDTO oldHabit = habitDTOS[0];
        HabitDTO newHabit = habitDTOS[1];

        if (!(userValidator.userExists(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to redact your habit!\"}");
        } else if (!(habitappRepository.hasHabit( userDTOToUser(userDTO), habitDTOToHabit(oldHabit) ))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to redact -  not found!\"}");
        }
        habitappRepository.changeHabit( userDTOToUser(userDTO), habitDTOToHabit(oldHabit), habitDTOToHabit(newHabit) );
    }

    public void removeHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!(userValidator.userExists(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to remove your habit!\"}");
        } else if (!(habitappRepository.hasHabit( userDTOToUser(userDTO), habitDTOToHabit(habitDTO) ))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to remove -  not found!\"}");
        }

        habitappRepository.removeHabit(userDTOToUser(userDTO), habitDTOToHabit(habitDTO));
    }

    public void createHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!(userValidator.userExists(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to remove your habit!\"}");
        } else if (habitappRepository.hasHabit( userDTOToUser(userDTO), habitDTOToHabit(habitDTO) )) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to create -  already exists!\"}");
        }

        habitappRepository.addNewHabit( userDTOToUser(userDTO), habitDTOToHabit(habitDTO) );
    }

    public void markHabit(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!(userValidator.userExists(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to remove your habit!\"}");
        } else if (habitappRepository.habitAlreadyMarked( userDTOToUser(userDTO), habitDTOToHabit(habitDTO) )) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "{\"message\": \"Habit is already marked!\"}");
        }

        habitappRepository.markHabit( userDTOToUser(userDTO), habitDTOToHabit(habitDTO) );
    }

    public List<LocalDateTime> getComplitedDays(HttpServletRequest req, HabitDTO habitDTO) throws UserIllegalRequestException {
        UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");

        if (!(userValidator.userExists(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not logged in to see habit statistics!\"}");
        } else if (!(habitappRepository.hasHabit( userDTOToUser(userDTO), habitDTOToHabit(habitDTO) ))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"Habit that you want to see for statistics -  not found!\"}");
        }

        return habitappRepository.getComplitedDays(userDTOToUser(userDTO), habitDTOToHabit(habitDTO));
    }


}
