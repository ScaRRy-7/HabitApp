package habitapp.services;

import habitapp.dto.HabitDTO;
import habitapp.enums.HabitFrequency;
import habitapp.exceptions.UserIllegalRequestException;
import jakarta.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

public interface HabitsService {

    public List<HabitDTO> getAllHabits(String authHeader);
    public void redactHabit(String authHeader, HabitDTO[] habitDTOS);
    public void deleteHabit(String authHeader, HabitDTO habitDTO);
    public void createHabit(String authHeader, HabitDTO habitDTO);
    public void markHabit(String authHeader, HabitDTO habitDTO);
    public void unmarkHabitsIfTimeLeft(String authHeader);
    public List<LocalDateTime> getCompletedDays(String authHeader, HabitDTO habitDTO);
    public HabitFrequency getFrequencyByName(String name);
}
