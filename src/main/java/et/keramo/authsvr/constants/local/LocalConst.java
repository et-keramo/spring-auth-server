package et.keramo.authsvr.constants.local;

public final class LocalConst {

    private LocalConst() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    public static final String JWT_SIGN_KEY = "gGcG3aW7oiFyrqPi4DE1Wp7Oh7PhhahRAwflb2EH7HQ=";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_SYSTEM = "SYS";

    // Path
    public static final String LOGIN_PATH = "/oauth/token";

    // JSON Object Key
    public static final String USERID_KEY = "uid";
    public static final String ROLES_KEY = "roles";

}
