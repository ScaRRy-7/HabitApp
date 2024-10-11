package adminstration;

import in.Reader;
import out.AdminUserChangerWriter;
import storage.UsersController;
import storage.UsersStorage;
import validate.AdminRemoverValidator;
import wait.Waiter;

public class AdminBlocator {

    private final UsersStorage usersStorage = UsersStorage.getInstance();
    private final Reader reader = new Reader();
    private final AdminUserChangerWriter writer = new AdminUserChangerWriter();
    private final Waiter waiter = new Waiter();
    private final AdminRemoverValidator validator = new AdminRemoverValidator();
    private final UsersController usersController = new UsersController();


    public void blockUser() {
        if (usersStorage.getUsers().isEmpty()) {
            writer.writeNoUsers();
            waiter.waitSecond();
            return;
        }

        writer.writeUsersToChoose();
        String userEmail = reader.read();
        if (validator.isValid(userEmail)) {
            usersController.blockUser(userEmail);
            writer.writeUserBlocked();
        } else {
            writer.reportUserNotFound();
            waiter.waitSecond();
        }
    }
}
