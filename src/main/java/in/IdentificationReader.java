package in;

import java.util.Scanner;

public final class IdentificationReader {

    private final Scanner scanner = new Scanner(System.in);

    public String readEmail() {
        String email = scanner.nextLine();
        return email;
    }
}
