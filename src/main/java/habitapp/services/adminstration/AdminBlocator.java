package services.adminstration;

import services.in.Reader;
import services.out.AdminUserChangerWriter;
import repositories.UsersRepository;
import repositories.UsersDAO;
import services.validate.AdminRemoverValidator;
import services.wait.Waiter;

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
    private final UsersDAO usersDAO;

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
    private final UsersRepository usersRepository;

    /**
     * Создает новый экземпляр класса AdminBlocator с использованием реальных объектов зависимостей.
     */
    public AdminBlocator() {
        usersDAO = UsersDAO.getInstance();
        reader = new Reader();
        writer = new AdminUserChangerWriter();
        validator = new AdminRemoverValidator();
        usersRepository = new UsersRepository();
    }

    /**
     * Создает новый экземпляр класса AdminBlocator с использованием mock-объектов зависимостей.
     *
     * @param usersDAOMock       mock-объект класса UsersStorage
     * @param readerMock             mock-объект класса Reader
     * @param writerMock             mock-объект класса AdminUserChangerWriter
     * @param waiterMock             mock-объект класса Waiter
     * @param validatorMock          mock-объект класса AdminRemoverValidator
     * @param usersRepositoryMock    mock-объект класса UsersController
     */
    public AdminBlocator(UsersDAO usersDAOMock, Reader readerMock, AdminUserChangerWriter writerMock, Waiter waiterMock, AdminRemoverValidator validatorMock, UsersRepository usersRepositoryMock) {
        this.usersDAO = usersDAOMock;
        this.reader = readerMock;
        this.writer = writerMock;
        this.validator = validatorMock;
        this.usersRepository = usersRepositoryMock;
    }

    /**
     * Блокирует указанного пользователя.
     */
    public void blockUser() {
        if (usersDAO.getUsers().isEmpty()) {
            writer.writeNoUsers();
            waiter.waitSecond();
            return;
        }

        writer.writeUsersToChoose();
        String userEmail = reader.read();
        if (validator.isValid(userEmail)) {
            usersRepository.blockUser(userEmail);
            writer.writeUserBlocked();
        } else {
            writer.reportUserNotFound();
            waiter.waitSecond();
        }
    }
}
