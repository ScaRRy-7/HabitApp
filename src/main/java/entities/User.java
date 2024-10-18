package entities;

import lombok.Getter;
import lombok.Setter;
import storage.UsersController;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс User представляет пользователя системы.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class User {

    UsersController usersController = new UsersController();

    @Setter
    @Getter
    private String name;

    @Setter
    @Getter
    private String email;

    @Setter
    @Getter
    private String password;


   //private List<Habit> habits = new ArrayList<>();

    private boolean isBlocked = false;
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public void addHabit(Habit habit) {
        usersController.addNewHabit(this, habit);
    }

    public void setBlocked() {
        isBlocked = true;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
