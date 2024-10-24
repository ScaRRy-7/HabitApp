package habitapp.services.out;
import habitapp.services.enums.Period;

public class ChooserStatisticsPeriodMenuWriter {

    public void writePeriods() {
        System.out.println("Список периодов, выбери тот, за который будет выгружена статистика:" +
                "\n\t1 - " + Period.DAY.getName() +
                "\n\t2 - " + Period.WEEK.getName() +
                "\n\t3 - " + Period.MONTH.getName());
    }

    public void reportInvalidPeriod() {
        System.out.println("Некорректно указан номер периода! напиши соответствующую цифру: ");
    }
}
