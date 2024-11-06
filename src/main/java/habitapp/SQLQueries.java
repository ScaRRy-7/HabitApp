package habitapp;

public class SQLQueries {

    public static final String INSERT_COMPLETED_DAY = "INSERT INTO habitschema.completed_days (habit_id) VALUES(?)";
    public static final String SELECT_COMPLETED_DATES = "SELECT completed_date FROM habitschema.completed_days WHERE habit_id = ?";
    public static final String SELECT_LAST_COMPLETED_DAY = "SELECT MAX(completed_date) AS last_completed_date FROM habitschema.completed_days WHERE habit_id = ?";
    public static final String SELECT_ALL_HABITS = "SELECT * FROM habitschema.habits WHERE user_id = ?";
    public static final String SELECT_HABIT = "SELECT * FROM habitschema.habits WHERE name = ? AND description = ? AND frequency = ? AND user_id = ?";
    public static final String DELETE_HABIT = "DELETE FROM habitschema.habits WHERE name = ? AND description = ? AND frequency = ? AND user_id = ?";
    public static final String UPDATE_HABIT = "UPDATE habitschema.habits SET name = ?, description = ?, frequency = ? WHERE name = ? AND description = ? AND frequency = ? AND user_id = ?";
    public static final String SELECT_HABIT_ID = "SELECT id FROM habitschema.habits WHERE name = ? AND description = ? AND frequency = ? AND user_id = ?";
    public static final String MARK_HABIT = "UPDATE habitschema.habits SET is_completed = true WHERE user_id = ?";
    public static final String SELECT_IS_COMPLETED = "SELECT is_completed FROM habitschema.habits WHERE id = ?";
    public static final String UNMARK_HABIT = "UPDATE habitschema.habits SET is_completed = false WHERE id = ?";
    public static final String INSERT_USER = "INSERT INTO habitschema.users (username, email, password) VALUES (?, ?, ?)";
    public static final String REDACT_USER = "UPDATE habitschema.users SET username = ?, email = ?, password = ? WHERE email = ?";
    public static final String DELETE_USER = "DELETE FROM habitschema.users WHERE email = ?";
    public static final String HAS_USER = "SELECT * FROM habitschema.users WHERE email = ?";
    public static final String SELECT_USER = "SELECT * FROM habitschema.users WHERE email = ?";
    public static final String SELECT_USER_ID = "SELECT id FROM habitschema.users WHERE email = ?";


}
