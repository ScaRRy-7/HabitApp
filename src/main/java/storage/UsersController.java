package storage;

import entities.Habit;
import entities.User;

public final class UsersController {

    private final UsersStorage usersStorage = UsersStorage.getInstance();

    public void addUserToDatabase(User user) {
        usersStorage.addUser(user);
    }

    public User getUserFromDatabase(String email) {
        return usersStorage.getUser(email);
    }

    public void updateRedactedUser(User user, String email) {
        usersStorage.removeUser(email);
        usersStorage.addUser(user);
    }

    public void removeUserFromDatabase(String email)  {
        usersStorage.removeUser(email);
    }

    public void addNewHabit(User user, Habit habit) {
        usersStorage.addHabitToUser(user, habit);
    }

    public void changeHabit(User user, Habit habit, int habitNumber) {
        int habitIndex = habitNumber-1;
        user.getHabits().set(habitIndex, habit);
        usersStorage.addUser(user);
    }

    public void removeHabit(User user, int habitNumber) {
        user.getHabits().remove(habitNumber-1);
        usersStorage.addUser(user);
    }

    public void blockUser(String email) {
        usersStorage.getUser(email).setBlocked();
    }
}
