package entities;

import enums.HabitFrequency;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Habit implements Comparable<Habit>{

    private String name;
    private String description;
    private HabitFrequency frequenсy;
    private LocalDateTime createdDateTime;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private boolean isComplited;
    private List<LocalDateTime> daysHabitComplited = new ArrayList<>();

    public Habit(String name, String description, HabitFrequency frequency) {
        this.name = name;
        this.description = description;
        this.frequenсy = frequency;
        this.createdDateTime = LocalDateTime.now();
        this.isComplited = false;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public HabitFrequency getFrequenсy() {
        return frequenсy;
    }
    public LocalDateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public List<LocalDateTime> getDaysHabitComplited() {
        return daysHabitComplited;
    }

    public boolean isComplited() {
        return isComplited;
    }

    public void setComplited() {
        isComplited = true;
    }

    public void setUncomplited() {
        isComplited = false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFrequenсy(HabitFrequency frequenсy) {
        this.frequenсy = frequenсy;
    }

    @Override
    public String toString() {
        return "Название привычки: " + getName() + "\n\tОписание привычки: " + getDescription() +
                    "\n\tЧастота: " + getFrequenсy().getName() + "\n\tСоздана: " +
                getCreatedDateTime().format(formatter) + "\n\tСтатус: " + (isComplited ? "Выполнена" : "Не выполнена");
    }


    @Override
    public int compareTo(Habit anotherHabit) {
        return this.createdDateTime.compareTo(anotherHabit.getCreatedDateTime());
    }
}
