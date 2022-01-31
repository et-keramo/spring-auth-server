package et.keramo.authsvr.exception;

import et.keramo.authsvr.api.rest.auth.OAuth2ErrorDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.ConnectException;

@NoArgsConstructor
@Getter
public class AuthServerException extends RuntimeException {

    public static final String INVALID_REQUEST = "invalid_request";
    public static final String CONNECT_REFUSED = "connect_refused";

    public String errorCode;

    public AuthServerException(String errorCode, String message, Throwable t) {
        super(message, t);
        this.errorCode = errorCode;
    }

    public AuthServerException(String message, Throwable t) {
        super(message, t);
    }

    public AuthServerException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return new OAuth2ErrorDto(this).toString();
    }

    public static AuthServerException create(Throwable t) {
        Throwable cause = t.getCause();

        if (cause instanceof ConnectException) {
            return new AuthServerException(CONNECT_REFUSED, "Connect Refused", cause);
        }
        return new AuthServerException(INVALID_REQUEST, "Auth Server Error", cause);
    }

}
