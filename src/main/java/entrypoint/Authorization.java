package entrypoint;

import actions.HabitsRedactor;
import actions.HabitsStatistics;
import actions.ProfileRedactor;
import in.AuthorizationReader;
import out.AuthorizationWriter;
import storage.User;
import validate.CommandAuthorizationValidator;
import wait.Waiter;

public final class Authorization {

    private final AuthorizationWriter writer = new AuthorizationWriter();
    private final AuthorizationReader reader = new AuthorizationReader();
    private final HabitsRedactor habitsRedactor = new HabitsRedactor();
    private final HabitsStatistics habitsStatistics = new HabitsStatistics();
    private final ProfileRedactor profileRedactor = new ProfileRedactor();
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
        String commandString = (reader.readCommand());
        int command = 0;

        if (commandValidator.isValid(commandString)) {
            command = Integer.parseInt(commandString);
        } else {
            writer.reportInvalidCommand();
            waiter.waitSecond();
            selectCommand();
        }

        switch (command) {
            case 1:
                profileRedactor.start(currentUser);
                break;
            case 2:
                habitsRedactor.start(currentUser);
                break;
            case 3:
                habitsStatistics.start(currentUser);
                break;
            case 4:
                Start.main(null);
                break;
        }
        selectCommand();
    }


}
