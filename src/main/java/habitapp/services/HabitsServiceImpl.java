package habitapp.services;

import habitapp.annotations.Loggable;
import habitapp.dto.HabitDTO;
import habitapp.dto.UserDTO;
import habitapp.entities.Habit;
import habitapp.entities.User;
import habitapp.enums.HabitFrequency;
import habitapp.exceptions.UserIllegalRequestException;
import habitapp.jwt.JwtUtil;
import habitapp.mappers.HabitMapper;
import habitapp.mappers.UserMapper;
import habitapp.repositories.CompletedDaysRepositoryImpl;
import habitapp.repositories.HabitsRepositoryImpl;
import habitapp.repositories.UsersRepositoryImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Сервис для работы с привычками.
 */
@RequiredArgsConstructor
@Service
@Slf4j
public class HabitsServiceImpl implements HabitsService, UserMapper, HabitMapper {

    private final UsersRepositoryImpl usersRepositoryImpl;
    private final HabitsRepositoryImpl habitsRepositoryImpl;
    private final CompletedDaysRepositoryImpl completedDaysRepositoryImpl;
    private final JwtUtil jwtUtil;

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


    @Override
    public List<HabitDTO> getAllHabits(String authHeader) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }
        int userId = usersRepositoryImpl.getUserId(userDTOToUser(userDTO));
        return habitsRepositoryImpl.getAllHabits(userId).stream()
                .map(this::habitToHabitDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void redactHabit(String authHeader, HabitDTO[] habitDTOS) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }
        HabitDTO oldHabit = habitDTOS[0];
        HabitDTO newHabit = habitDTOS[1];

        if (!usersRepositoryImpl.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not authorized to edit your habit!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitDTOToHabit(oldHabit), usersRepositoryImpl.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"The habit you want to edit was not found!\"}");
        }
        habitsRepositoryImpl.editHabit(habitDTOToHabit(oldHabit), habitDTOToHabit(newHabit), usersRepositoryImpl.getUserId(userDTOToUser (userDTO)));
    }


    @Override
    public void deleteHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }

        if (!usersRepositoryImpl.hasUser  (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not authorized to delete your habit!\"}");
        } else if (!(habitsRepositoryImpl.hasHabit(habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userDTOToUser (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"The habit you want to delete was not found!\"}");
        }
        habitsRepositoryImpl.deleteHabit(habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userDTOToUser (userDTO)));
    }


    @Override
    public void createHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        };

        if (!(usersRepositoryImpl.hasUser (userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not authorized to create a new habit!\"}");
        } else if (habitsRepositoryImpl.hasHabit(habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "{\"message\": \"The habit you want to create already exists!\"}");
        }
        int userId = usersRepositoryImpl.getUserId(userDTOToUser (userDTO));
        habitsRepositoryImpl.createHabit(habitDTOToHabit(habitDTO), userId);
    }


    @Override
    public void markHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }
        unmarkHabitsIfTimeLeft(authHeader);

        if (!usersRepositoryImpl.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not authorized to mark your habit!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"The habit you want to mark was not found!\"}");
        } else if (habitsRepositoryImpl.isMarked(habitsRepositoryImpl.getHabitId(habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userDTOToUser  (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT, "{\"message\": \"The habit you want to mark is already marked!\"}");
        }

        int userId = usersRepositoryImpl.getUserId(userDTOToUser (userDTO));
        int habitId = habitsRepositoryImpl.getHabitId(habitDTOToHabit(habitDTO), userId);

        habitsRepositoryImpl.markHabit(habitDTOToHabit(habitDTO), userId);
        completedDaysRepositoryImpl.createCompletedDay(habitId);
    }


    @Override
    public List<LocalDateTime> getCompletedDays(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }

        if (!usersRepositoryImpl.hasUser (userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "{\"message\": \"You are not authorized to view your habit statistics!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND, "{\"message\": \"The habit you want to view statistics for was not found!\"}");
        }
        int habitId = habitsRepositoryImpl.getHabitId(habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userDTOToUser (userDTO)));
        return completedDaysRepositoryImpl.getCompletedDays(habitId);
    }


    @Override
    public void unmarkHabitsIfTimeLeft(String authHeader) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }

        List<HabitDTO> habitDTOS = habitsRepositoryImpl.getAllHabits(usersRepositoryImpl.getUserId(userDTOToUser (userDTO)))
                .stream().map(this::habitToHabitDTO).toList();

        int userId = usersRepositoryImpl.getUserId(userDTOToUser (userDTO));
        for (HabitDTO habitDTO : habitDTOS) {
            int habitId = habitsRepositoryImpl.getHabitId(habitDTOToHabit(habitDTO), userId);
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
                    log.debug("habit with id {} was unmarked", habitId);
                }
            } else {
                log.debug("No completed days found for habit (id = {}). Skipping unmarking.", habitId);
            }
        }
    }
    @Override
    public HabitFrequency getFrequencyByName(String name) {
        for (HabitFrequency frequency : HabitFrequency.values()) {
            if (frequency.getName().equals(name)) {
                return frequency;
            }
        }
        throw new IllegalArgumentException("this habit frequency does not exist!: " + name);
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