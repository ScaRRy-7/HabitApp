package out;

import entities.User;
import storage.UsersStorage;

import java.util.Collection;
import java.util.Set;

public class AdminUserChangerWriter {

    private final UsersStorage usersStorage = UsersStorage.getInstance();

    public void writeUsersToChoose() {
        System.out.println("Список пользователей:");
        Collection<User> users =  usersStorage.getUsers();
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
