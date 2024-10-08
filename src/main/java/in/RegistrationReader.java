package in;

import java.util.Scanner;

public final class RegistrationReader {

    private static final RegistrationReader registrationReader = new RegistrationReader();
    private final Scanner scanner = new Scanner(System.in);

    private RegistrationReader() {}

    public static RegistrationReader getInstance() {
        return registrationReader;
    }

    public String getName() {
        String name = scanner.nextLine();
        return name;
    }

    public String getPassword() {
        String password = scanner.nextLine();
        return password;
    }
}
