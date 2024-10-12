package adminstration;

import in.Reader;
import out.AdminUserChangerWriter;
import storage.UsersController;
import storage.UsersStorage;
import validate.AdminRemoverValidator;
import wait.Waiter;

public class AdminBlocator {

    private final UsersStorage usersStorage;
    private final Reader reader;
    private final AdminUserChangerWriter writer;
    private final Waiter waiter = new Waiter();
    private final AdminRemoverValidator validator;
    private final UsersController usersController;

    public AdminBlocator() {
        usersStorage = UsersStorage.getInstance();
        reader = new Reader();
        writer = new AdminUserChangerWriter();
        validator = new AdminRemoverValidator();
        usersController = new UsersController();
    }

    public AdminBlocator(UsersStorage usersStorageMock, Reader readerMock, AdminUserChangerWriter writerMock, Waiter waiterMock, AdminRemoverValidator validatorMock, UsersController usersControllerMock) {
        this.usersStorage = usersStorageMock;
        this.reader = readerMock;
        this.writer = writerMock;
        this.validator = validatorMock;
        this.usersController = usersControllerMock;
    }

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
