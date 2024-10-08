package out;

public final class RegistrationWriter {

    private static final RegistrationWriter registrationWriter = new RegistrationWriter();

    private RegistrationWriter() {}

    public static RegistrationWriter getInstance() {
        return registrationWriter;
    }
}
