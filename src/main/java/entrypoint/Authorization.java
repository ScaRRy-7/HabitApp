package entrypoint;

import out.AuthorizationWriter;

public final class Authorization {

    private static final Authorization authorization = new Authorization();
    private final AuthorizationWriter writer = AuthorizationWriter.getInstance();

    private Authorization() {}

    public static Authorization getInstance() {
        return authorization;
    }

    public void start() {

    }


}
