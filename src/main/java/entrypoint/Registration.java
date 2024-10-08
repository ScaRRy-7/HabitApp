package entrypoint;

import out.RegistrationWriter;

public final class Registration {

    private static final Registration registration = new Registration();
    private final RegistrationWriter writer = RegistrationWriter.getInstance();

    private Registration() {}

    public static Registration getInstance() {
        return registration;
    }

    public void registrate(String email) {

    }





}
