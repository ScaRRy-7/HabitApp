package out;

public class AuthorizationWriter {

    private static final AuthorizationWriter authorizationWriter = new AuthorizationWriter();

    private AuthorizationWriter() {}

    public static AuthorizationWriter getInstance() {
        return authorizationWriter;
    }
}
