package in;

public class AuthorizationReader {

    private static final AuthorizationReader instance = new AuthorizationReader();

    private AuthorizationReader() {}

    public static AuthorizationReader getInstance() {
        return instance;
    }
}
