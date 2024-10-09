package entities;

import java.util.ArrayList;
import java.util.List;

public final class User {

    private String name;
    private String email;
    private String password;
    private List<Habit> habits = new ArrayList<>();

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public List<Habit> getHabits() {
        return habits;
    }
}
