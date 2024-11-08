package habitapp.mappers;

import habitapp.dto.HabitDTO;
import habitapp.entities.Habit;
import org.mapstruct.Mapper;
import org.springframework.stereotype.Component;


@Component
@Mapper(componentModel = "spring")
public interface HabitMapper {


    HabitDTO habitToHabitDTO(Habit habit);
    Habit habitDTOToHabit(HabitDTO habitDTO);
}