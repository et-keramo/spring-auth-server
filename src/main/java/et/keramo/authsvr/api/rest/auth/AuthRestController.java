package et.keramo.authsvr.api.rest.auth;

import et.keramo.authsvr.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 인증 API 공통
 */
@RequiredArgsConstructor
@RequestMapping("/oauth")
public abstract class AuthRestController {

    private final AuthService authService;

    @PostMapping("token")
    public Object login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.authService.login(request, response);
    }

    @PostMapping("validation")
    public Object validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.authService.validate(request, response);
    }

    @PostMapping("revoke")
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return this.authService.logout(request, response);
    }

}
