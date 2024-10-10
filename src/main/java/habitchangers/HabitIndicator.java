package habitchangers;

import entities.User;
import out.HabitsRedactorWriter;
import wait.Waiter;

public class HabitIndicator {

    private final HabitsRedactorWriter writer = new HabitsRedactorWriter();
    private final Waiter waiter = new Waiter();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();

    public void showHabits(User currentUser) {
        habitUnmarker.checkHabits(currentUser); // ПРЕДВАРИТЕЛЬНО РАЗМАРКИРОВКА ПРИВЫЧЕК ЕСЛИ ПРОШЕЛ СРОК (ДЕНЬ ИЛИ МЕСЯЦ)
        if (currentUser.getHabits().isEmpty()) {
            writer.infoNoHabits();
        } else {
            writer.writeHabits(currentUser);
        }
        waiter.waitSecond();
    }
}
