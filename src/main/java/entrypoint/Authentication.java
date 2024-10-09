package entrypoint;

import checkers.ExitChecker;
import checkers.PasswordChecker;
import in.Reader;
import menus.AuthorizationMenu;
import out.AuthenticationWriter;
import entities.User;
import storage.UsersController;
import validate.PasswordValidator;
import wait.Waiter;

public final class Authentication {

    private final AuthenticationWriter writer = new AuthenticationWriter();
    private final Reader reader = new Reader();
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final Waiter waiter = new Waiter();
    private final PasswordChecker passwordChecker = new PasswordChecker();
    private final AuthorizationMenu authorizationMenu = new AuthorizationMenu();
    private final UsersController usersController = new UsersController();

    public void login(String email) {
        writer.askPassword();
        String password = reader.read();

        // если пользователь вдруг решил выйти
        ExitChecker.check(password);

        if (passwordValidator.isValid(password)) {
            if (passwordChecker.checkPassword(email, password)) {
                User user = usersController.getUserFromDatabase(email);
                authorizationMenu.start(user);
            } else {
                writer.infoIncorrectPassword();
                waiter.waitSecond();
                login(email);
            }
        } else {
            writer.reportInvalidPassword();
            waiter.waitSecond();
            login(email);
        }

    }
}
