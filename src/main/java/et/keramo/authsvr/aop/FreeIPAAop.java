package et.keramo.authsvr.aop;

import et.keramo.authsvr.constants.keycloak.FreeIPAConst;
import et.keramo.authsvr.service.user.FreeIPAUserService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Aspect
public class FreeIPAAop {

    private static final String USER_KEY = "user";
    private static final String PASSWORD_KEY = "password";

    @Before("@annotation(et.keramo.authsvr.annotation.FreeIPAAPI)")
    public void preAuthentication(JoinPoint joinPoint) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add(USER_KEY, "admin");
        body.add(PASSWORD_KEY, "meta1234!@#$");

        FreeIPAUserService userService = (FreeIPAUserService) joinPoint.getThis();

        String path = userService.makePath(FreeIPAConst.SESSION_PATH);
        userService.postRequest(path, body);
    }

}
