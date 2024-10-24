package habitapp.services.out;

import habitapp.entities.User;
import habitapp.repositories.HabitappDAO;

import java.util.Collection;

public class AdminUserChangerWriter {

    private final HabitappDAO habitappDAO = HabitappDAO.getInstance();

    public void writeUsersToChoose() {
        System.out.println("Список пользователей:");
        Collection<User> users =  habitappDAO.getUsers();
        for (User user : users) {
            System.out.println("почта: " + user.getEmail() + " ник: " + user.getName() + "\n");
        }
        System.out.println("Напишите почту пользователя: ");
    }

    public void writeNoUsers() {
        System.out.println("Нет созданных пользователей!");
    }

    public void writeUserRemoved() {
        System.out.println("Пользователь успешно удален!");
    }

    public void reportUserNotFound() {
        System.out.println("Пользователь с такой почтой не найден!");
    }

    public void writeUserBlocked() {
        System.out.println("Пользователь успешно заблокирован!");
    }
}
