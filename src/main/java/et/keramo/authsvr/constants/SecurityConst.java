package et.keramo.authsvr.constants;

import java.util.Arrays;
import java.util.List;

public final class SecurityConst {

    private SecurityConst() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BASIC_AUTH_PREFIX = "Basic";

    public static final String LOCAL_TYPE = "LOCAL";
    public static final String KEYCLOAK_TYPE = "KEYCLOAK";

    public static final List<String> SUPPORT_AUTH = Arrays.asList(
            LOCAL_TYPE,
            KEYCLOAK_TYPE
    );

    public static final String AUTHORIZATION_CODE_GRANT = "authorization_code";
    public static final String IMPLICIT_GRANT = "implicit";
    public static final String PASSWORD_GRANT = "password";
    public static final String CLIENT_CREDENTIALS_GRANT = "client_credentials";
    public static final String REFRESH_TOKEN_GRANT = "refresh_token";

    public static final String[] SUPPORT_GRANT_TYPES = {
            AUTHORIZATION_CODE_GRANT,
            IMPLICIT_GRANT,
            PASSWORD_GRANT,
            CLIENT_CREDENTIALS_GRANT,
            REFRESH_TOKEN_GRANT
    };

}
