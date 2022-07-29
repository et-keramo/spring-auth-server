package et.keramo.authsvr.constants.keycloak;

public final class FreeIPAConst {

    private FreeIPAConst() {
        throw new IllegalStateException("Cannot create instance of static util class");
    }

    // Path
    public static final String SESSION_PATH = "/login_password";
    public static final String JSON_RPC_PATH = "/json";

    // API Method
    public static final String USER_LIST_METHOD = "user_find";
    public static final String USER_GET_METHOD = "user_show";
    public static final String USER_ADD_METHOD = "user_add";
    public static final String USER_UPDATE_METHOD = "user_mod";
    public static final String USER_DELETE_METHOD = "user_del";
    public static final String PASSWD_METHOD = "passwd";
    public static final String GROUP_LIST_METHOD = "group_find";
    public static final String GROUP_GET_METHOD = "group_show";
    public static final String GROUP_ADD_METHOD = "group_add";
    public static final String GROUP_UPDATE_METHOD = "group_mod";
    public static final String GROUP_DELETE_METHOD = "group_del";

    // API Parameter Key
    public static final String METHOD_KEY = "method";
    public static final String PARAMS_KEY = "params";

    // JSON Object Key
    public static final String UID_KEY = "uid";
    public static final String GIVENNAME_KEY = "givenname";
    public static final String SN_KEY = "sn";
    public static final String CN_KEY = "cn";
    public static final String MAIL_KEY = "mail";


}
