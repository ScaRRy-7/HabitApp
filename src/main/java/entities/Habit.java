package entities;

import enums.HabitFrequency;

public class Habit {

    private String name;
    private String description;
    private HabitFrequency frequenсy;

    public Habit(String name, String description, HabitFrequency frequency) {
        this.name = name;
        this.description = description;
        this.frequenсy = frequency;
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
                    "\n\tЧастота: "+ getFrequenсy().getName();
    }
}
