package et.keramo.authsvr.constants.keycloak;

public final class KeycloakConst {

    private KeycloakConst() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    // Path
    public static String API_PATH(String realm) {
        return String.format("/auth/realms/%s", realm);
    }
    public static String ADMIN_API_PATH(String realm) {
        return String.format("/auth/admin/realms/%s", realm);
    }
    public static String CLIENT_PATH(String id) {
        return String.format(CLIENT_PATH + "/%s", id);
    }
    public static String DEFAULT_SCOPE_PATH(String id, String scopeId) {
        return String.format(CLIENT_PATH + "/%s" + DEFAULT_SCOPE_PATH + "/%s", id, scopeId);
    }
    public static String OPTIONAL_SCOPE_PATH(String id, String scopeId) {
        return String.format(CLIENT_PATH + "/%s" + OPTIONAL_SCOPE_PATH + "/%s", id, scopeId);
    }

    public static final String LOGIN_PATH = "/protocol/openid-connect/token";
    public static final String VALIDATION_PATH = "/protocol/openid-connect/token/introspect";
    public static final String LOGOUT_PATH = "/protocol/openid-connect/revoke";
    public static final String CLIENT_PATH = "/clients";
    public static final String CLIENT_SECRET_PATH = "/client-secret";
    public static final String CLIENT_SCOPE_PATH = "/client-scopes";
    public static final String DEFAULT_SCOPE_PATH = "/default-client-scopes";
    public static final String OPTIONAL_SCOPE_PATH = "/optional-client-scopes";

    // API Parameter Key
    public static final String GRANT_TYPE_PARAM = "grant_type";
    public static final String CLIENT_ID_PARAM = "client_id";
    public static final String USERNAME_PARAM = "username";
    public static final String PASSWORD_PARAM = "password";

    // JSON Object Key
    public static final String TOKEN_KEY = "access_token";
    public static final String CLIENT_ID_KEY = "clientId";
    public static final String ATTRIBUTES_KEY = "attributes";
    public static final String AUTHORIZATION_CODE_GRANT_KEY = "standardFlowEnabled";
    public static final String IMPLICIT_GRANT_KEY = "implicitFlowEnabled";
    public static final String PASSWORD_GRANT_KEY = "directAccessGrantsEnabled";
    public static final String CLIENT_CREDENTIALS_GRANT_KEY = "serviceAccountsEnabled";
    public static final String REFRESH_TOKEN_GRANT_KEY = "use.refresh.tokens";
    public static final String ACCESS_TIMEOUT_KEY = "client.session.idle.timeout";
    public static final String REFRESH_TIMEOUT_KEY = "client.session.max.lifespan";
    public static final String ID_KEY = "id";
    public static final String REDIRECT_URIS_KEY = "redirectUris";
    public static final String WEB_ORIGINS_KEY = "webOrigins";
    public static final String DEFAULT_SCOPES_KEY = "defaultClientScopes";
    public static final String OPTIONAL_SCOPES_KEY = "optionalClientScopes";

}
