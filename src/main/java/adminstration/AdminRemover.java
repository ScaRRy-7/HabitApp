package adminstration;

import in.Reader;
import out.AdminUserChangerWriter;
import storage.UsersStorage;
import validate.AdminRemoverValidator;
import wait.Waiter;

/**
 * Класс AdminRemover отвечает за удаление пользователей администратором.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class AdminRemover {
    /**
     * Объект класса UsersStorage для хранения пользователей.
     */
    private final UsersStorage usersStorage = UsersStorage.getInstance();

    /**
     * Объект класса Reader для чтения ввода пользователя.
     */
    private final Reader reader = new Reader();

    /**
     * Объект класса AdminUserChangerWriter для записи сообщений для администратора.
     */
    private final AdminUserChangerWriter writer = new AdminUserChangerWriter();

    /**
     * Объект класса Waiter для ожидания определенного времени.
     */
    private final Waiter waiter = new Waiter();

    /**
     * Объект класса AdminRemoverValidator для валидации ввода пользователя.
     */
    private final AdminRemoverValidator validator = new AdminRemoverValidator();

    /**
     * Удаляет пользователя, указанного администратором.
     */
    public void removeUser() {
        if (usersStorage.getUsers().isEmpty()) {
            writer.writeNoUsers();
            waiter.waitSecond();
            return;
        }

        writer.writeUsersToChoose();
        String userEmail = reader.read();
        if (validator.isValid(userEmail)) {
            usersStorage.removeUser(userEmail);
            writer.writeUserRemoved();
        } else {
            writer.reportUserNotFound();
            waiter.waitSecond();
        }
    }
}
