package habitapp.mappers;

import habitapp.dto.HabitDTO;
import habitapp.entities.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HabitMapper {
    HabitMapper INSTANCE = Mappers.getMapper(HabitMapper.class);

    HabitDTO habitToHabitDTO(Habit habit);
    Habit habitDTOToHabit(HabitDTO habitDTO);
}
