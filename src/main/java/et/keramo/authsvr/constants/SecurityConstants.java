package et.keramo.authsvr.constants;

import java.util.Arrays;
import java.util.List;

public final class SecurityConstants {

    private SecurityConstants() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BASIC_AUTH_PREFIX = "Basic";

    public static final List<String> SUPPORT_AUTH = Arrays.asList(
            SecurityConstants.LOCAL_AUTH_TYPE,
            SecurityConstants.KEYCLOAK_AUTH_TYPE
    );

    // LOCAL
    public static final String LOCAL_AUTH_TYPE = "LOCAL";
    public static final String LOCAL_LOGIN_PATH = "/oauth/token";

    public static final String JWT_SIGN_KEY = "gGcG3aW7oiFyrqPi4DE1Wp7Oh7PhhahRAwflb2EH7HQ=";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String ROLE_SYSTEM = "SYS";

    // KEYCLOAK
    public static final String KEYCLOAK_AUTH_TYPE = "KEYCLOAK";
    public static final String KEYCLOAK_LOGIN_PATH = "/protocol/openid-connect/token";
    public static final String KEYCLOAK_VALIDATION_PATH = "/protocol/openid-connect/token/introspect";
    public static final String KEYCLOAK_LOGOUT_PATH = "/protocol/openid-connect/revoke";

}
