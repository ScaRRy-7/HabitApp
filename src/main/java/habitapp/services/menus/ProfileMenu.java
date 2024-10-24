//package habitapp.services.menus;
//
//import habitapp.entities.User;
//import habitapp.repositories.UsersRepository;
//import habitapp.services.entrypoint.Start;
//import habitapp.services.validate.CommandProfileValidator;
//import habitapp.services.enums.ProfileCommand;
//import habitapp.services.in.Reader;
//import habitapp.services.out.ProfileRedactorWriter;
//import habitapp.services.wait.Waiter;
//
///**
// * Отвечает за предоставление пользователю меню для управления его профилем.
// * Это меню позволяет пользователю выполнять следующие действия:
// * 1. Изменение имени
// * 2. Изменение электронной почты
// * 3. Изменение пароля
// * 4. Удаление аккаунта
// * 5. Возврат в предыдущее меню
// * 6. Выход из приложения
// *
// * @author ScaRRy-7
// * @version 1.0
// */
//public class ProfileMenu implements Commander {
//
//    private final CommandProfileValidator commandValidator = new CommandProfileValidator();
//    private final PasswordValidator passwordValidator = new PasswordValidator();
//    private final ProfileRedactorWriter writer = new ProfileRedactorWriter();
//    private final Reader reader = new Reader();
//    private final UsersRepository usersRepository = new UsersRepository();
//    private final EmailValidator emailValidator = new EmailValidator();
//    private final NameValidator nameValidator = new NameValidator();
//    private final Waiter waiter = new Waiter();
//    private User currentUser;
//
//    /**
//     * Запускает меню управления профилем для авторизованного пользователя.
//     * Вызывает метод {@link #selectCommand()} для отображения списка команд и обработки выбранной пользователем команды.
//     *
//     * @param user авторизованный пользователь
//     */
//    public void start(User user) {
//        currentUser = user;
//        selectCommand();
//    }
//
//    /**
//     * Отображает список команд меню управления профилем и обрабатывает выбранную пользователем команду.
//     * Если пользователь выбрал корректную команду, выполняется соответствующая функциональность.
//     * Если пользователь выбрал некорректную команду, пользователю предлагается ввести команду еще раз.
//     */
//    public void selectCommand() {
//        writer.writeCommands();
//        String commandString = reader.read();
//        ProfileCommand command;
//
//        if (commandValidator.isValid(commandString)) {
//            command = getProfileCommandByNumber(Integer.parseInt(commandString));
//            switch (command) {
//                case CHANGENAME:
//                    changeName();
//                    break;
//                case CHANGEEMAIL:
//                    changeEmail();
//                    break;
//                case CHANGEPASSWORD:
//                    changePassword();
//                    break;
//                case DELETEACCOUNT:
//                    deleteAccount();
//                    break;
//                case RETURNTOMENU:
//                    return;
//                case EXIT:
//                    Start.main(null);
//                    break;
//            }
//        } else {
//            writer.reportInvalidCommand();
//            waiter.waitSecond();
//
//        }
//        selectCommand();
//    }
//
//    /**
//     * Преобразует число, введенное пользователем, в соответствующую команду меню управления профилем.
//     *
//     * @param commandNumber номер команды, введенный пользователем
//     * @return соответствующая команда меню
//     * @throws IllegalArgumentException если введен некорректный номер команды
//     */
//    private ProfileCommand getProfileCommandByNumber(int commandNumber) {
//        return switch (commandNumber) {
//            case 1 -> ProfileCommand.CHANGENAME;
//            case 2 -> ProfileCommand.CHANGEEMAIL;
//            case 3 -> ProfileCommand.CHANGEPASSWORD;
//            case 4 -> ProfileCommand.DELETEACCOUNT;
//            case 5 -> ProfileCommand.RETURNTOMENU;
//            case 6 -> ProfileCommand.EXIT;
//            default -> throw new IllegalArgumentException("Invalid command number");
//        };
//    }
//
///**
// * Позволяет пользователю изменить свое имя.
//
// * Новое имя проверяется на валидность, и если оно корректно, оно сохраняется в профиле пользователя.
// * Если имя некорректно, пользователю предлагается ввести имя еще раз.
// */
//public void changeName() {
//    writer.askName();
//    String name = reader.read();
//
//    if (nameValidator.isValid(name)) {
//        currentUser.setName(name);
//        usersRepository.updateRedactedUser(currentUser, currentUser.getEmail());
//        writer.infoNameChanged();
//        waiter.waitSecond();
//    } else {
//        writer.reportInvalidName();
//        waiter.waitSecond();
//        changeName();
//    }
//}
//
//    /**
//     * Позволяет пользователю изменить свой email.
//     * Новый email проверяется на валидность, и если он корректен, он сохраняется в профиле пользователя.
//     * Если email некорректен, пользователю предлагается ввести email еще раз.
//     */
//    public void changeEmail() {
//        writer.askEmail();
//        String email = reader.read();
//
//        if (emailValidator.isValid(email)) {
//            String oldEmail = currentUser.getEmail();
//            currentUser.setEmail(email);
//            usersRepository.updateRedactedUser(currentUser, oldEmail);
//            writer.infoEmailChanged();
//            waiter.waitSecond();
//        } else {
//            writer.reportInvalidEmail();
//            waiter.waitSecond();
//            changeEmail();
//        }
//    }
//
//    /**
//     * Позволяет пользователю изменить свой пароль.
//     * Новый пароль проверяется на валидность, и если он корректен, он сохраняется в профиле пользователя.
//     * Если пароль некорректен, пользователю предлагается ввести пароль еще раз.
//     */
//    public void changePassword() {
//        writer.askPassword();
//        String password = reader.read();
//
//        if (passwordValidator.isValid(password)) {
//            currentUser.setPassword(password);
//            usersRepository.updateRedactedUser(currentUser, currentUser.getEmail());
//            writer.infoPasswordChanged();
//            waiter.waitSecond();
//        } else {
//            writer.reportInvalidPassword();
//            waiter.waitSecond();
//            changePassword();
//        }
//    }
//
//    /**
//     * Позволяет пользователю удалить свой аккаунт.
//     * Пользователь удаляется из базы данных, и приложение возвращается в главное меню.
//     */
//    public void deleteAccount() {
//        usersRepository.removeUserFromDatabase(currentUser.getEmail());
//        writer.infoAccDeleted();
//        waiter.waitSecond();
//        Start.main(null);
//    }
//}
