package habitapp.services.validate;

public class StatisticsPeriodMenuValidator {

    public boolean isValidPeriod(String periodString) {
        return switch (periodString) {
            case "1", "2", "3" -> true;
            default -> false;
        };
    }
}
