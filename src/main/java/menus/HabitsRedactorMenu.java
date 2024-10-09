package menus;

import entities.*;
import enums.HabitCommand;
import habitchangers.HabitCreator;
import habitchangers.HabitEditor;
import habitchangers.HabitIndicator;
import habitchangers.HabitRemover;
import in.Reader;
import out.HabitsRedactorWriter;
import storage.UsersController;
import validate.CommandHabitValidator;
import wait.Waiter;

public class HabitsRedactorMenu implements Commander {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final Reader reader = new Reader();
    private final Waiter waiter = new Waiter();
    private final UsersController usersController = new UsersController();
    private User currentUser;
    private final HabitEditor habitEditor = new HabitEditor();
    private final HabitCreator habitCreator = new HabitCreator();
    private final HabitIndicator habitIndicator = new HabitIndicator();
    private final HabitRemover habitRemover = new HabitRemover();

    public void start(User user) {
        currentUser = user;
        selectCommand();
    }

    public void selectCommand() {
        writer.writeCommands();
        String commandString = reader.read();
        HabitCommand command;

        if (habitValidator.isValidCommand(commandString)) {
            command = getHabitCommandByNumber(Integer.parseInt(commandString));
            switch (command) {
                case CREATEHABIT:
                    habitCreator.createNewHabit(currentUser);
                    break;
                case REDACTHABIT:
                    habitEditor.redactHabit(currentUser);
                    break;
                case DELETEHABIT:
                    habitRemover.removeHabit(currentUser);
                    break;
                case SHOWHMYHABITS:
                    habitIndicator.showHabits(currentUser);
                    break;
                case RETURNTOMENU:
                   return;
            }
        } else {
            writer.reportInvalidCommand();
            waiter.waitSecond();
        }
        selectCommand();
    }

    private HabitCommand getHabitCommandByNumber(int commandNumber) {
        return switch (commandNumber) {
            case 1 -> HabitCommand.CREATEHABIT;
            case 2 -> HabitCommand.REDACTHABIT;
            case 3 -> HabitCommand.DELETEHABIT;
            case 4 -> HabitCommand.SHOWHMYHABITS;
            case 5 -> HabitCommand.RETURNTOMENU;
            default -> throw new IllegalArgumentException("Invalid command number");
        };
    }


}
