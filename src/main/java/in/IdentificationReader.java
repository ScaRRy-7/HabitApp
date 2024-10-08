package in;

import java.util.Scanner;

public final class IdentificationReader {

    private static final IdentificationReader reader = new IdentificationReader();
    private final Scanner scanner = new Scanner(System.in);

    private IdentificationReader() {}

    public static IdentificationReader getInstance() {
        return reader;
    }

    public String readEmail() {
        String email = scanner.nextLine();
        return email;
    }
}
