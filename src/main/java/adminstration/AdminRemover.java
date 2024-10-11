package adminstration;

import in.Reader;
import out.AdminUserChangerWriter;
import storage.UsersStorage;
import validate.AdminRemoverValidator;
import wait.Waiter;

public class AdminRemover {

    private final UsersStorage usersStorage = UsersStorage.getInstance();
    private final Reader reader = new Reader();
    private final AdminUserChangerWriter writer = new AdminUserChangerWriter();
    private final Waiter waiter = new Waiter();
    private final AdminRemoverValidator validator = new AdminRemoverValidator();

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
