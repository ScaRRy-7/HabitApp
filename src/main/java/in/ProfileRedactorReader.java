package in;

import java.util.Scanner;

public class ProfileRedactorReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readCommand() {
        String command = scanner.nextLine();
        return command;
    }

    public String readName() {
        String name = scanner.nextLine();
        return name;
    }

    public String readEmail() {
        String email = scanner.nextLine();
        return email;
    }

    public String readPassword() {
        String password = scanner.nextLine();
        return password;
    }
}
