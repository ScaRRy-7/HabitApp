package in;

import java.util.Scanner;

public class AuthenticationReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readPassword() {
        String password = scanner.nextLine();
        return password;
    }
}
