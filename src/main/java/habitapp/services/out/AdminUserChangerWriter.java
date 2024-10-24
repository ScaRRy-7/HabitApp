package services.out;

import services.entities.User;
import repositories.UsersDAO;

import java.util.Collection;

public class AdminUserChangerWriter {

    private final UsersDAO usersDAO = UsersDAO.getInstance();

    public void writeUsersToChoose() {
        System.out.println("Список пользователей:");
        Collection<User> users =  usersDAO.getUsers();
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
