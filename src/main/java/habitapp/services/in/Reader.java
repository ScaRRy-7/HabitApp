package habitapp.services.in;

import java.util.Scanner;

public class Reader {

    private final Scanner scanner = new Scanner(System.in);

    public String read() {
        return scanner.nextLine();
    }
}
