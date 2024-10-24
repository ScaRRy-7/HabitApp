package services.entities;

import lombok.Getter;
import lombok.Setter;
import repositories.UsersRepository;

/**
 * Класс User представляет пользователя системы.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public final class User {

    UsersRepository usersRepository = new UsersRepository();

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
        usersRepository.addNewHabit(this, habit);
    }

    public void setBlocked() {
        isBlocked = true;
    }

    public boolean isBlocked() {
        return isBlocked;
    }
}
