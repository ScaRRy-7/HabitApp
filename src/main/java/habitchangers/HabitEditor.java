package habitchangers;

import entities.Habit;
import entities.User;
import enums.HabitFrequency;
import enums.PartOfHabit;
import in.Reader;
import out.HabitEditorWriter;
import storage.UsersController;
import validate.CommandHabitValidator;
import validate.HabitEditorValidator;
import wait.Waiter;
import org.slf4j.*;

public class HabitEditor {

    private final Reader reader = new Reader();
    private final HabitEditorWriter writer = new HabitEditorWriter();
    private final CommandHabitValidator habitValidator = new CommandHabitValidator();
    private final HabitEditorValidator validator = new HabitEditorValidator();
    private final UsersController usersController = new UsersController();
    private final Waiter waiter = new Waiter();
    private final HabitUnmarker habitUnmarker = new HabitUnmarker();
    private final Logger logger = LoggerFactory.getLogger(HabitEditor.class);

    public void redactHabit(User currentUser) {
        logger.info("Запущен выбор привычки которую пользователь будет редактировать");
        if (usersController.getAllHabits(currentUser).isEmpty()) {
            logger.debug("У пользователя отсутствуют привычки, выбор невозможен");
            writer.infoNoHabits();
            waiter.waitSecond();
        } else {
            writer.askNumberOfHabitRedact(currentUser);
            String habitNumberStr = reader.read();
            if (habitValidator.isValidHabitNumber(currentUser, habitNumberStr)) {
                logger.info("Пользователь ввел корректный номер привычки для редактирования");
                int habitNumber = Integer.parseInt(habitNumberStr);
                startRedact(currentUser, habitNumber);
                writer.infoHabitRedacted();
                waiter.waitSecond();
            } else {
                logger.info("Пользователь ввел некорректный номери привычки для редактирования, выбор будет запущен снова");
                writer.reportInvalidHabitNumberRedact();
                waiter.waitSecond();
                redactHabit(currentUser);
            }
        }
    }

    private void startRedact(User user, int habitNumber) {
        logger.info("Запущено редактирование, пользователь выбирает что именно изменить в привычке");
        writer.askWhatChange();
        String numberStr = reader.read();
        if (validator.isValidNumberForRedacting(numberStr)) {
            logger.info("Пользователь ввел корректный номер части привычки");
            PartOfHabit partOfHabit = getPartOfHabit(numberStr);
            switch (partOfHabit) {
                case NAME:
                    logger.info("Пользователь выбрал редактирование имени привычки");
                    redactName(user, habitNumber);
                    break;
                case DESCRIPTION:
                    logger.info("Пользователь выбрал редактирование описания привычки");
                    redactDescription(user, habitNumber);
                    break;
                case FREQUENCY:
                    logger.info("Пользователь выбрал редактирование частоты выполнения привычки");
                    redactFrequency(user, habitNumber);
                    break;
            }
        } else {
            logger.debug("Пользователь ввел некорректный номер части привычки, запрос будет повторен");
            writer.reportIncorrectNumberForEditing();
            waiter.waitSecond();
            startRedact(user, habitNumber);
        }
    }

    public void redactName(User user, int habitNumber) {
        logger.info("Запущен запрос нового имени привычки");
        writer.askNewHabitName();
        String habitName = reader.read();

        if (validator.isValidHabitName(habitName)) {
            logger.info("Пользователь ввел валидное новое имя привычки");
            Habit newHabit = usersController.getHabitFromUser(user, habitNumber);
            newHabit.setName(habitName);
            usersController.changeHabit(user, newHabit, habitNumber);
        } else {
            logger.info("Пользователь ввел невалидное новое имя привычки, имя будет запрошено снова");
            writer.reportIncorrectHabitName();
            waiter.waitSecond();
            redactName(user, habitNumber);
        }
    }

    public void redactDescription(User user, int habitNumber) {
        logger.info("Запущен запрос нового описания привычки");
        writer.askNewHabitDescription();
        String habitDescription = reader.read();

        if (validator.isValidHabitDescription(habitDescription)) {
            logger.info("Пользователь ввел валидное новое описание привычки");
            Habit newHabit = usersController.getHabitFromUser(user, habitNumber);
            newHabit.setDescription(habitDescription);
            usersController.changeHabit(user, newHabit, habitNumber);
        } else {
            logger.info("Пользователь ввел невалидное новое описание привычки, описание будет запрошено снова");
            writer.reportIncorrectHabitDescription();
            waiter.waitSecond();
            redactDescription(user, habitNumber);
        }
    }

    public void redactFrequency(User user, int habitNumber) {
        logger.info("Запущен запрос новой частоты выполнения привычки");
        writer.askNewHabitFrequency();
        String habitFrequencyNumber = reader.read();

        if (validator.isValidHabitFrequencyNumber(habitFrequencyNumber)) {
            logger.info("Пользователь ввел валидный номер частоты");
            HabitFrequency newHabitFrequency = getFrequencyByNumber(habitFrequencyNumber);
            Habit newHabit = usersController.getHabitFromUser(user, habitNumber);
            newHabit.setFrequenсy(newHabitFrequency);
            usersController.changeHabit(user, newHabit, habitNumber);
        } else {
            logger.info("Пользователь ввел невалидный номер новой частоты, номер будет снова");
            writer.reportIncorrectFrequencyNumber();
            waiter.waitSecond();
            redactFrequency(user, habitNumber);
        }
    }

    public PartOfHabit getPartOfHabit(String numberStr) {
        return switch (numberStr) {
            case "1" -> PartOfHabit.NAME;
            case "2" -> PartOfHabit.DESCRIPTION;
            case "3" -> PartOfHabit.FREQUENCY;
            default -> throw new IllegalArgumentException("Invalid number");
        };
    }

    public HabitFrequency getFrequencyByNumber(String numberStr) {
        return switch (numberStr) {
            case "1" -> HabitFrequency.DAILY;
            case "2" -> HabitFrequency.WEEKLY;
            default -> throw new IllegalArgumentException("Invalid number");
        };
    }


}
