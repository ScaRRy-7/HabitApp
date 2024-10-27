package habitapp.mappers;

import habitapp.dto.UserDTO;
import habitapp.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper для преобразования объектов пользователя.
 * <p>
 * Этот интерфейс использует MapStruct для преобразования между сущностями
 * `User` и их представлениями `UserDTO`. Он обеспечивает простой способ
 * конвертации данных между различными слоями приложения.
 * </p>
 */
@Mapper
public interface UserMapper {
    /**
     * Экземпляр маппера.
     */
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    /**
     * Преобразует сущность пользователя в DTO.
     *
     * @param user Сущность пользователя, которую необходимо преобразовать.
     * @return Объект `UserDTO`, полученный из сущности.
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    UserDTO userToUserDTO(User user);

    /**
     * Преобразует DTO пользователя в сущность.
     *
     * @param userDTO Объект `UserDTO`, который необходимо преобразовать.
     * @return Сущность `User`, полученная из DTO.
     */
    @Mapping(source = "name", target = "name")
    @Mapping(source = "email", target = "email")
    @Mapping(source = "password", target = "password")
    User userDTOToUser(UserDTO userDTO);
}
