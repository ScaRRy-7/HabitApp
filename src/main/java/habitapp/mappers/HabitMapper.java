package habitapp.mappers;

import habitapp.dto.HabitDTO;
import habitapp.entities.Habit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования объектов привычек.
 * <p>
 * Этот интерфейс использует MapStruct для преобразования между сущностями
 * `Habit` и их представлениями `HabitDTO`. Он обеспечивает простой способ
 * конвертации данных между различными слоями приложения.
 * </p>
 */
@Mapper
public interface HabitMapper {
    /**
     * Экземпляр маппера.
     */
    HabitMapper INSTANCE = Mappers.getMapper(HabitMapper.class);

    /**
     * Преобразует сущность привычки в DTO.
     *
     * @param habit Сущность привычки, которую необходимо преобразовать.
     * @return Объект `HabitDTO`, полученный из сущности.
     */
    HabitDTO habitToHabitDTO(Habit habit);

    /**
     * Преобразует DTO привычки в сущность.
     *
     * @param habitDTO Объект `HabitDTO`, который необходимо преобразовать.
     * @return Сущность `Habit`, полученная из DTO.
     */
    Habit habitDTOToHabit(HabitDTO habitDTO);
}
