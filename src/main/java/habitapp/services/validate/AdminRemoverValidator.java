package habitapp.services.validate;

import habitapp.repositories.HabitappDAO;

public class AdminRemoverValidator {

    private final HabitappDAO habitappDAO = HabitappDAO.getInstance();

    public boolean isValid(String email) {
        return habitappDAO.hasUser(email);
    }
}
