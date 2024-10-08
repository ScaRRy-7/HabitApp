package out;

import in.IdentificationReader;

public class IdentificationWriter {

    private static final IdentificationWriter identificationWriter = new IdentificationWriter();

    private IdentificationWriter() {}

    public static IdentificationWriter getInstance() {
        return identificationWriter;
    }

    public void writeGreetings() {
        System.out.println("Введи свою почту, а я проверю, есть ли ты в базе");
    }

    public void reportInvalidEmail() {
        System.out.println("Ты ввел некорректную почту! Попробуй ещё раз");
    }
}
