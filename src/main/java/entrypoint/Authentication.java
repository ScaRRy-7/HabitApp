package entrypoint;

public class Authentication {

    private static final Authentication authentication = new Authentication();

    private Authentication() {}

    public static Authentication getInstance() {
        return authentication;
    }

    public void login(String email) {

    }
}
