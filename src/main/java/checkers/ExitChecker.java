package checkers;

import entrypoint.Identification;


public class ExitChecker {

    private static final Identification identification = new Identification();

    public static void check(String str) {
        if (str.equals("exit".toLowerCase())) {
            identification.start();
        }
    }
}
