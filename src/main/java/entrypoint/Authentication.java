package entrypoint;

import checker.ExitChecker;
import checker.PasswordChecker;
import in.AuthenticationReader;
import out.AuthenticationWriter;
import storage.User;
import storage.UsersController;
import validate.PasswordValidator;
import wait.Waiter;

public final class Authentication {

    private final AuthenticationWriter writer = new AuthenticationWriter();
    private final AuthenticationReader reader = new AuthenticationReader();
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final Waiter waiter = new Waiter();
    private final PasswordChecker passwordChecker = new PasswordChecker();
    private final Authorization authorization = new Authorization();
    private final UsersController usersController = new UsersController();

    public void login(String email) {
        writer.askPassword();
        String password = reader.readPassword();

        // если пользователь вдруг решил выйти
        ExitChecker.check(password);

        if (passwordValidator.isValid(password)) {
            if (passwordChecker.checkPassword(email, password)) {
                User user = usersController.getUserFromDatabase(email);
                authorization.start(user);
            }
        } else {
            writer.reportInvalidPassword();
            waiter.waitSecond();
            login(email);
        }

    }
}
