package menus;

import enums.ProfileCommand;
import entrypoint.Start;
import in.Reader;
import out.ProfileRedactorWriter;
import entities.User;
import storage.UsersController;
import validate.CommandProfileValidator;
import validate.EmailValidator;
import validate.NameValidator;
import validate.PasswordValidator;
import wait.Waiter;

public class ProfileMenu implements Commander {

    private final CommandProfileValidator commandValidator = new CommandProfileValidator();
    private final PasswordValidator passwordValidator = new PasswordValidator();
    private final ProfileRedactorWriter writer = new ProfileRedactorWriter();
    private final Reader reader = new Reader();
    private final UsersController usersController = new UsersController();
    private final EmailValidator emailValidator = new EmailValidator();
    private final NameValidator nameValidator = new NameValidator();
    private final Waiter waiter = new Waiter();
    private User currentUser;

    public void start(User user) {
        currentUser = user;
        selectCommand();
    }

    public void selectCommand() {
        writer.writeCommands();
        String commandString = reader.read();
        ProfileCommand command;

        if (commandValidator.isValid(commandString)) {
            command = getProfileCommandByNumber(Integer.parseInt(commandString));
            switch (command) {
                case CHANGENAME:
                    changeName();
                    break;
                case CHANGEEMAIL:
                    changeEmail();
                    break;
                case CHANGEPASSWORD:
                    changePassword();
                    break;
                case DELETEACCOUNT:
                    deleteAccount();
                    break;
                case RETURNTOMENU:
                    return;
                case EXIT:
                    Start.main(null);
                    break;
            }
        } else {
            writer.reportInvalidCommand();
            waiter.waitSecond();

        }
        selectCommand();
    }

    private ProfileCommand getProfileCommandByNumber(int commandNumber) {
        return switch (commandNumber) {
            case 1 -> ProfileCommand.CHANGENAME;
            case 2 -> ProfileCommand.CHANGEEMAIL;
            case 3 -> ProfileCommand.CHANGEPASSWORD;
            case 4 -> ProfileCommand.DELETEACCOUNT;
            case 5 -> ProfileCommand.RETURNTOMENU;
            case 6 -> ProfileCommand.EXIT;
            default -> throw new IllegalArgumentException("Invalid command number");
        };
    }

    public void changeName() {
        writer.askName();
        String name = reader.read();

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
        String email = reader.read();

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
        String password = reader.read();

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
