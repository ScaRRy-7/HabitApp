package habitapp.entities;

import lombok.Getter;
import lombok.Setter;
import habitapp.repositories.HabitappRepository;

/**
 * Класс User представляет пользователя системы.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class User {

    HabitappRepository habitappRepository = new HabitappRepository();

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String password;

    private boolean isBlocked = false;

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }


    public void setBlocked() {
        isBlocked = true;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
