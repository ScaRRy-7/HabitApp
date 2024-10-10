package habitchangers;

import entities.User;
import in.Reader;
import out.HabitsRedactorWriter;
import storage.UsersController;
import validate.CommandHabitValidator;
import wait.Waiter;

public class HabitRemover {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final Waiter waiter = new Waiter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final Reader reader = new Reader();
    private final UsersController usersController = new UsersController();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    public void removeHabit(User currentUser) {
        if (currentUser.getHabits().isEmpty()) {
            writer.infoNoHabits();
            waiter.waitSecond();
        } else {
            habitUnmarker.checkHabits(currentUser); // ПРЕДВАРИТЕЛЬНО РАЗМАРКИРОВКА ПРИВЫЧЕК ЕСЛИ ПРОШЕЛ СРОК (ДЕНЬ ИЛИ МЕСЯЦ)
            writer.askNumberOfHabitRemove(currentUser);
            String habitNumberStr = reader.read();
            if (habitValidator.isValidHabitNumber(currentUser, habitNumberStr)) {
                int habitNumber = Integer.parseInt(habitNumberStr);
                usersController.removeHabit(currentUser, habitNumber);
                writer.infoHabitRemoved();
                waiter.waitSecond();
            } else {
                writer.reportInvalidHabitNumberRemove();
                waiter.waitSecond();
                removeHabit(currentUser);
            }
        }
    }
}
