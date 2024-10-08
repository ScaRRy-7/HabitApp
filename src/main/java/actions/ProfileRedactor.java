package actions;

import entrypoint.Start;
import in.ProfileRedactorReader;
import out.ProfileRedactorWriter;
import storage.User;
import storage.UsersController;
import validate.CommandProfileValidator;
import validate.EmailValidator;
import validate.NameValidator;
import validate.PasswordValidator;
import wait.Waiter;

public class ProfileRedactor {

    private final CommandProfileValidator commandValidator = new CommandProfileValidator();
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final ProfileRedactorWriter writer = new ProfileRedactorWriter();
    private final ProfileRedactorReader reader = new ProfileRedactorReader();
    private final UsersController usersController = new UsersController();
    private final EmailValidator emailValidator = new EmailValidator();
    private final NameValidator nameValidator = new NameValidator();

    private final Waiter waiter = new Waiter();
    private User currentUser;

    public void start(User user) {
        currentUser = user;
        writer.writeCommands();
        selectCommand();
    }

    public void selectCommand() {
        String commandString = reader.readCommand();
        int command = 0;

        if (commandValidator.isValid(commandString)) {
            command = Integer.parseInt(commandString);
            switch (command) {
                case 1:
                    changeName();
                    break;
                case 2:
                    changeEmail();
                    break;
                case 3:
                    changePassword();
                    break;
                case 4:
                    deleteAccount();
                    break;
                case 5:
                    Start.main(null);
                    break;
            }
        } else {
            writer.reportInvalidCommand();
            waiter.waitSecond();
            selectCommand();
        }

    }

    public void changeName() {
        writer.askName();
        String name = reader.readName();

        if (nameValidator.isValid(name)) {
            currentUser.setName(name);
            usersController.updateRedactedUser(currentUser, currentUser.getEmail());
            writer.infoNameChanged();
            waiter.waitSecond();
        } else {
            writer.reportInvalidName();
            waiter.waitSecond();
            changeName();
        }
    }

    public void changeEmail() {
        writer.askEmail();
        String email = reader.readEmail();

        if (emailValidator.isValid(email)) {
            String oldEmail = currentUser.getEmail();
            currentUser.setEmail(email);
            usersController.updateRedactedUser(currentUser, oldEmail);
            writer.infoEmailChanged();
            waiter.waitSecond();
        } else {
            writer.reportInvalidEmail();
            waiter.waitSecond();
            changeEmail();
        }
    }

    public void changePassword() {
        writer.askPassword();
        String password = reader.readPassword();

        if (passwordValidator.isValid(password)) {
            currentUser.setPassword(password);
            usersController.updateRedactedUser(currentUser, currentUser.getEmail());
            writer.infoPasswordChanged();
            waiter.waitSecond();
        } else {
            writer.reportInvalidPassword();
            waiter.waitSecond();
            changePassword();
        }
    }

    public void deleteAccount() {
        usersController.removeUserFromDatabase(currentUser.getEmail());
        writer.infoAccDeleted();
        waiter.waitSecond();
        Start.main(null);

    }
}
