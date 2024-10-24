package habitapp.services.adminstration;

import habitapp.services.in.Reader;
import habitapp.services.out.AdminUserChangerWriter;
import habitapp.repositories.HabitappDAO;
import habitapp.services.validate.AdminRemoverValidator;
import habitapp.services.wait.Waiter;

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
    private final HabitappDAO habitappDAO = HabitappDAO.getInstance();

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
        if (habitappDAO.getUsers().isEmpty()) {
            writer.writeNoUsers();
            waiter.waitSecond();
            return;
        }

        writer.writeUsersToChoose();
        String userEmail = reader.read();
        if (validator.isValid(userEmail)) {
            habitappDAO.removeUser(userEmail);
            writer.writeUserRemoved();
        } else {
            writer.reportUserNotFound();
            waiter.waitSecond();
        }
    }
}
