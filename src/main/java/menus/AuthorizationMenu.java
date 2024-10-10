package menus;

import entrypoint.Start;
import in.Reader;
import out.AuthorizationWriter;
import entities.User;
import validate.CommandAuthorizationValidator;
import wait.Waiter;
import enums.MenuCommand;

public final class AuthorizationMenu implements Commander {

    private final AuthorizationWriter writer = new AuthorizationWriter();
    private final Reader reader = new Reader();
    private final HabitsRedactorMenu habitsRedactorMenu = new HabitsRedactorMenu();
    private final HabitsStatisticsMenu habitsStatisticsMenu = new HabitsStatisticsMenu();
    private final ProfileMenu profileMenu = new ProfileMenu();
    private final CommandAuthorizationValidator commandValidator = new CommandAuthorizationValidator();
    private final Waiter waiter = new Waiter();
    private User currentUser;

    public void start(User user) {
        currentUser = user;
        writer.greetings(user);
        selectCommand();
    }

    public void selectCommand() {
        writer.getCommands();
        String commandString = (reader.read());
        MenuCommand command;

        if (commandValidator.isValid(commandString)) {
            command = getMenuCommandByNumber(Integer.parseInt(commandString));
            switch (command) {
                case REDACTPROFILE:
                    profileMenu.start(currentUser);
                    break;
                case MANAGEHABITS:
                    habitsRedactorMenu.start(currentUser);
                    break;
                case HABITSTATISTICS:
                    habitsStatisticsMenu.start(currentUser);
                    break;
                case EXIT:
                    Start.main(null);
                    break;
            }
        } else {
            writer.reportInvalidCommand();
            waiter.waitSecond();
            selectCommand();
        }
        selectCommand();
    }

    private MenuCommand getMenuCommandByNumber(int commandNumber) {
        return switch (commandNumber) {
            case 1 -> MenuCommand.REDACTPROFILE;
            case 2 -> MenuCommand.MANAGEHABITS;
            case 3 -> MenuCommand.HABITSTATISTICS;
            case 4 -> MenuCommand.EXIT;
            default -> throw new IllegalArgumentException("Invalid command number");
        };
    }


}
