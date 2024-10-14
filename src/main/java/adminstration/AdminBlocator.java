package adminstration;

import in.Reader;
import out.AdminUserChangerWriter;
import storage.UsersController;
import storage.UsersStorage;
import validate.AdminRemoverValidator;
import wait.Waiter;

/**
 * Класс AdminBlocator отвечает за блокировку пользователей администратором.
 *
 * @author ScaRRy-7
 * @version 1.0
 */
public class AdminBlocator {
    /**
     * Объект класса UsersStorage для хранения пользователей.
     */
    private final UsersStorage usersStorage;

    /**
     * Объект класса Reader для чтения ввода пользователя.
     */
    private final Reader reader;

    /**
     * Объект класса AdminUserChangerWriter для записи сообщений для администратора.
     */
    private final AdminUserChangerWriter writer;

    /**
     * Объект класса Waiter для ожидания определенного времени.
     */
    private final Waiter waiter = new Waiter();

    /**
     * Объект класса AdminRemoverValidator для валидации ввода пользователя.
     */
    private final AdminRemoverValidator validator;

    /**
     * Объект класса UsersController для взаимодействия с пользователями.
     */
    private final UsersController usersController;

    /**
     * Создает новый экземпляр класса AdminBlocator с использованием реальных объектов зависимостей.
     */
    public AdminBlocator() {
        usersStorage = UsersStorage.getInstance();
        reader = new Reader();
        writer = new AdminUserChangerWriter();
        validator = new AdminRemoverValidator();
        usersController = new UsersController();
    }

    /**
     * Создает новый экземпляр класса AdminBlocator с использованием mock-объектов зависимостей.
     *
     * @param usersStorageMock       mock-объект класса UsersStorage
     * @param readerMock             mock-объект класса Reader
     * @param writerMock             mock-объект класса AdminUserChangerWriter
     * @param waiterMock             mock-объект класса Waiter
     * @param validatorMock          mock-объект класса AdminRemoverValidator
     * @param usersControllerMock    mock-объект класса UsersController
     */
    public AdminBlocator(UsersStorage usersStorageMock, Reader readerMock, AdminUserChangerWriter writerMock, Waiter waiterMock, AdminRemoverValidator validatorMock, UsersController usersControllerMock) {
        this.usersStorage = usersStorageMock;
        this.reader = readerMock;
        this.writer = writerMock;
        this.validator = validatorMock;
        this.usersController = usersControllerMock;
    }

    /**
     * Блокирует указанного пользователя.
     */
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
