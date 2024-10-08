package in;

import java.util.Scanner;

public final class RegistrationReader {

    private final Scanner scanner = new Scanner(System.in);

    public String getName() {
        String name = scanner.nextLine();
        return name;
    }

    public String getPassword() {
        String password = scanner.nextLine();
        return password;
    }
}
