package in;

import java.util.Scanner;

public class AuthorizationReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readCommand() {
        String command = scanner.nextLine();
        return command;
    }
}
