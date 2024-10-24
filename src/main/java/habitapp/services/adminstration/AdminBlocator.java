package habitapp.services.adminstration;

import habitapp.services.in.Reader;
import habitapp.services.out.AdminUserChangerWriter;
import habitapp.repositories.HabitappRepository;
import habitapp.repositories.HabitappDAO;
import habitapp.services.validate.AdminRemoverValidator;
import habitapp.services.wait.Waiter;

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
    private final HabitappDAO habitappDAO;

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
    private final HabitappRepository habitappRepository;

    /**
     * Создает новый экземпляр класса AdminBlocator с использованием реальных объектов зависимостей.
     */
    public AdminBlocator() {
        habitappDAO = HabitappDAO.getInstance();
        reader = new Reader();
        writer = new AdminUserChangerWriter();
        validator = new AdminRemoverValidator();
        habitappRepository = new HabitappRepository();
    }

    /**
     * Создает новый экземпляр класса AdminBlocator с использованием mock-объектов зависимостей.
     *
     * @param habitappDAOMock       mock-объект класса UsersStorage
     * @param readerMock             mock-объект класса Reader
     * @param writerMock             mock-объект класса AdminUserChangerWriter
     * @param waiterMock             mock-объект класса Waiter
     * @param validatorMock          mock-объект класса AdminRemoverValidator
     * @param habitappRepositoryMock    mock-объект класса UsersController
     */
    public AdminBlocator(HabitappDAO habitappDAOMock, Reader readerMock, AdminUserChangerWriter writerMock, Waiter waiterMock, AdminRemoverValidator validatorMock, HabitappRepository habitappRepositoryMock) {
        this.habitappDAO = habitappDAOMock;
        this.reader = readerMock;
        this.writer = writerMock;
        this.validator = validatorMock;
        this.habitappRepository = habitappRepositoryMock;
    }

    /**
     * Блокирует указанного пользователя.
     */
    public void blockUser() {
        if (habitappDAO.getUsers().isEmpty()) {
            writer.writeNoUsers();
            waiter.waitSecond();
            return;
        }

        writer.writeUsersToChoose();
        String userEmail = reader.read();
        if (validator.isValid(userEmail)) {
            habitappRepository.blockUser(userEmail);
            writer.writeUserBlocked();
        } else {
            writer.reportUserNotFound();
            waiter.waitSecond();
        }
    }
}
