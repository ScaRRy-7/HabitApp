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
import io.jsonwebtoken.ExpiredJwtException;
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
public class HabitsServiceImpl implements HabitsService {

    private final UsersRepositoryImpl usersRepositoryImpl;
    private final HabitsRepositoryImpl habitsRepositoryImpl;
    private final CompletedDaysRepositoryImpl completedDaysRepositoryImpl;
    private final JwtUtil jwtUtil;

    private final UserMapper userMapper;
    private final HabitMapper habitMapper;


    @Override
    public List<HabitDTO> getAllHabits(String authHeader) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }
        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser(userDTO));
        return habitsRepositoryImpl.getAllHabits(userId).stream()
                .map(habitMapper::habitToHabitDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void redactHabit(String authHeader, HabitDTO[] habitDTOS) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }
        HabitDTO oldHabit = habitDTOS[0];
        HabitDTO newHabit = habitDTOS[1];

        if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser(userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"You are not authorized to edit your habit!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(oldHabit), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"The habit you want to edit was not found!\"}");
        }
        habitsRepositoryImpl.editHabit(habitMapper.habitDTOToHabit(oldHabit), habitMapper.habitDTOToHabit(newHabit),
                usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)));
    }


    @Override
    public void deleteHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }

        if (!usersRepositoryImpl.hasUser  (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"You are not authorized to delete your habit!\"}");
        } else if (!(habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"The habit you want to delete was not found!\"}");
        }
        habitsRepositoryImpl.deleteHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)));
    }


    @Override
    public void createHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        };

        if (!(usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"You are not authorized to create a new habit!\"}");
        } else if (habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT,
                    "{\"message\": \"The habit you want to create already exists!\"}");
        }
        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO));
        habitsRepositoryImpl.createHabit(habitMapper.habitDTOToHabit(habitDTO), userId);
    }


    @Override
    public void markHabit(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }
        unmarkHabitsIfTimeLeft(authHeader);

        if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"You are not authorized to mark your habit!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"The habit you want to mark was not found!\"}");
        } else if (habitsRepositoryImpl.isMarked(habitsRepositoryImpl.getHabitId(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser  (userDTO))))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_CONFLICT,
                    "{\"message\": \"The habit you want to mark is already marked!\"}");
        }

        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO));
        int habitId = habitsRepositoryImpl.getHabitId(habitMapper.habitDTOToHabit(habitDTO), userId);

        habitsRepositoryImpl.markHabit(habitMapper.habitDTOToHabit(habitDTO), userId);
        completedDaysRepositoryImpl.createCompletedDay(habitId);
    }


    @Override
    public List<LocalDateTime> getCompletedDays(String authHeader, HabitDTO habitDTO) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }

        if (!usersRepositoryImpl.hasUser (userMapper.userDTOToUser (userDTO))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED,
                    "{\"message\": \"You are not authorized to view your habit statistics!\"}");
        } else if (!habitsRepositoryImpl.hasHabit(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_NOT_FOUND,
                    "{\"message\": \"The habit you want to view statistics for was not found!\"}");
        }
        int habitId = habitsRepositoryImpl.getHabitId(habitMapper.habitDTOToHabit(habitDTO), usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)));
        return completedDaysRepositoryImpl.getCompletedDays(habitId);
    }


    @Override
    public void unmarkHabitsIfTimeLeft(String authHeader) throws UserIllegalRequestException {
        String email = validateTokenAndGetEmail(authHeader);
        UserDTO userDTO = userMapper.userToUserDTO(usersRepositoryImpl.getUser(email));
        if (userDTO == null) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
        }

        List<HabitDTO> habitDTOS = habitsRepositoryImpl.getAllHabits(usersRepositoryImpl.getUserId(userMapper.userDTOToUser (userDTO)))
                .stream().map(habitMapper::habitToHabitDTO).toList();

        int userId = usersRepositoryImpl.getUserId(userMapper.userDTOToUser(userDTO));
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
        String email;
        try {
            email = jwtUtil.extractUsername(token);
        } catch (ExpiredJwtException e) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_BAD_REQUEST, "Token has expired :" + e.getMessage());
        }

        if (email == null || !jwtUtil.validateToken(token, email)) {
            throw new UserIllegalRequestException(HttpServletResponse.SC_UNAUTHORIZED, "Invalid token");
        }

        return email;
    }
}