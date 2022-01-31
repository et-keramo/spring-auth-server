package et.keramo.authsvr.api.rest.auth;

import et.keramo.authsvr.exception.AuthServerException;
import et.keramo.authsvr.service.auth.AbstractAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/oauth")
public class OAuth2RestController {

    private final AbstractAuthService authService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public Object login(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return this.authService.login(request, response);
        } catch (AuthServerException e) {
            return new OAuth2ErrorDto(e);
        }
    }

    @RequestMapping(value = "validation", method = RequestMethod.POST)
    public Object validation(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return this.authService.login(request, response);
        } catch (AuthServerException e) {
            return new OAuth2ErrorDto(e);
        }
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public Object logout(HttpServletRequest request, HttpServletResponse response) throws Exception {
        try {
            return this.authService.login(request, response);
        } catch (AuthServerException e) {
            return new OAuth2ErrorDto(e);
        }
    }

}
