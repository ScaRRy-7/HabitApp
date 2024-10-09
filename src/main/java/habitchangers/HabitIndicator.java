package habitchangers;

import entities.User;
import out.HabitsRedactorWriter;
import wait.Waiter;

public class HabitIndicator {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final Waiter waiter = new Waiter();

    public void showHabits(User currentUser) {
        if (currentUser.getHabits().isEmpty()) {
            writer.infoNoHabits();
        } else {
            writer.writeHabits(currentUser);
        }
        waiter.waitSecond();
    }
}
